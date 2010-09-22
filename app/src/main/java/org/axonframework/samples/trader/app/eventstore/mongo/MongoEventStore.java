package org.axonframework.samples.trader.app.eventstore.mongo;

import com.mongodb.*;
import org.axonframework.domain.DomainEvent;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.SimpleDomainEventStream;
import org.axonframework.eventstore.*;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * db.things.ensureIndex({firstname: 1, lastname: 1}, {unique: true});
 *
 * @author Jettro Coenradie
 */
public class MongoEventStore implements SnapshotEventStore, EventStoreManagement {
    private static final Logger logger = LoggerFactory.getLogger(MongoEventStore.class);
    private static final int EVENT_VISITOR_BATCH_SIZE = 50;
    private final MongoHelper mongo;
    private final EventSerializer eventSerializer;
    private final WriteConcern writeConcern;

    public MongoEventStore(EventSerializer eventSerializer, MongoHelper mongo, String testContext) {
        this.eventSerializer = eventSerializer;
        this.mongo = mongo;
        boolean testEnabled = Boolean.parseBoolean(testContext);
        if (testEnabled) {
            writeConcern = WriteConcern.SAFE;
            logger.debug("Mongo event store created for test environment");
        } else {
            writeConcern = WriteConcern.REPLICAS_SAFE;
            logger.debug("Mongo event store created for production environment");
        }
    }

    public MongoEventStore(MongoHelper mongo, String testContext) {
        this(new XStreamEventSerializer(), mongo, testContext);
    }

    @Override
    public void appendEvents(String type, DomainEventStream events) {
        List<DBObject> entries = new ArrayList<DBObject>();
        while (events.hasNext()) {
            DomainEvent event = events.next();
            DomainEventEntry entry = new DomainEventEntry(type, event, eventSerializer);
            DBObject mongoEntry = BasicDBObjectBuilder.start()
                    .add("aggregateIdentifier", entry.getAggregateIdentifier().toString())
                    .add("sequenceNumber", entry.getSequenceNumber())
                    .add("timeStamp", entry.getTimeStamp().toString())
                    .add("type", entry.getType())
                    .add("serializedEvent", entry.getSerializedEvent())
                    .get();
            entries.add(mongoEntry);
        }
        mongo.domainEvents().insert(entries.toArray(new DBObject[entries.size()]), writeConcern);
        logger.debug("Event of type {} appended", type);
    }

    @Override
    public DomainEventStream readEvents(String type, UUID identifier) {
        long snapshotSequenceNumber = -1;
        SnapshotEventEntry lastSnapshotEvent = loadLastSnapshotEvent(type, identifier);
        if (lastSnapshotEvent != null) {
            snapshotSequenceNumber = lastSnapshotEvent.getSequenceNumber();
        }

        List<DomainEvent> events = readEventSegmentInternal(type, identifier, snapshotSequenceNumber + 1);
        if (lastSnapshotEvent != null) {
            events.add(0, lastSnapshotEvent.getDomainEvent(eventSerializer));
        }
        if (events.isEmpty()) {
            throw new EventStreamNotFoundException(
                    String.format("Aggregate of type [%s] with identifier [%s] cannot be found.",
                            type,
                            identifier.toString()));
        }
        return new SimpleDomainEventStream(events);
    }

    public DomainEventStream readEventSegment(String type, UUID identifier, long firstSequenceNumber) {
        return new SimpleDomainEventStream(readEventSegmentInternal(type, identifier, firstSequenceNumber));
    }

    private List<DomainEvent> readEventSegmentInternal(String type, UUID identifier, long firstSequenceNumber) {
        DBObject mongoEntry = BasicDBObjectBuilder.start()
                .add("aggregateIdentifier", identifier)
                .add("sequenceNumber", firstSequenceNumber)
                .add("type", type)
                .get();

        DBCursor dbCursor = mongo.domainEvents().find(mongoEntry);
        List<DomainEvent> events = new ArrayList<DomainEvent>(dbCursor.size());
        while (dbCursor.hasNext()) {
            events.add(eventSerializer.deserialize((byte[]) dbCursor.next().get("serializedEvent")));
        }
        return events;
    }

    private SnapshotEventEntry loadLastSnapshotEvent(String type, UUID identifier) {
        DBObject mongoEntry = BasicDBObjectBuilder.start()
                .add("aggregateIdentifier", identifier.toString())
                .add("type", type)
                .get();
        DBCursor dbCursor = mongo.domainEvents().find(mongoEntry).sort(new BasicDBObject("sequenceNumber", -1));

        if (!dbCursor.hasNext()) {
            return null;
        }
        DBObject first = dbCursor.next();
        SnapshotEventEntry snapshot = new SnapshotEventEntry(
                (String) first.get("aggregateIdentifier"),
                (Long) first.get("sequenceNumber"),
                (byte[]) first.get("serializedEvent"),
                (String) first.get("timeStamp"),
                (String) first.get("type")
        );
        return snapshot;
    }

    @Override
    public void appendSnapshotEvent(String type, DomainEvent snapshotEvent) {
        SnapshotEventEntry snapshotEventEntry = new SnapshotEventEntry(type, snapshotEvent, eventSerializer);
        DBObject mongoSnapshotEntry = BasicDBObjectBuilder.start()
                .add("aggregateIdentifier", snapshotEventEntry.getAggregateIdentifier())
                .add("sequenceNumber", snapshotEventEntry.getSequenceNumber())
                .add("serializedEvent", snapshotEventEntry.getSerializedEvent())
                .add("timeStamp", snapshotEventEntry.getTimeStamp())
                .add("type", snapshotEventEntry.getType())
                .get();
        mongo.snapshotEvents().insert(mongoSnapshotEntry, writeConcern);
    }


    @Override
    public void visitEvents(EventVisitor visitor) {
        int first = 0;
        List<DomainEventEntry> batch;
        boolean shouldContinue = true;
        while (shouldContinue) {
            batch = fetchBatch(first, EVENT_VISITOR_BATCH_SIZE);
            for (DomainEventEntry entry : batch) {
                visitor.doWithEvent(entry.getDomainEvent(eventSerializer));
            }
            shouldContinue = (batch.size() >= EVENT_VISITOR_BATCH_SIZE);
            first += EVENT_VISITOR_BATCH_SIZE;
        }

    }

    private List<DomainEventEntry> fetchBatch(int startPosition, int batchSize) {
        DBObject sort = BasicDBObjectBuilder.start()
                .add("timeStamp", -1)
                .add("sequenceNumber", -1)
                .get();
        DBCursor batchDomainEvents = mongo.domainEvents().find().sort(sort).limit(batchSize).skip(startPosition);
        List<DomainEventEntry> entries = new ArrayList<DomainEventEntry>();
        while (batchDomainEvents.hasNext()) {
            DBObject dbObject = batchDomainEvents.next();
            DomainEventEntry entry = createDomainEventEntry(dbObject);
            entries.add(entry);
        }
        return entries;
    }

    private DomainEventEntry createDomainEventEntry(DBObject dbObject) {
        return new DomainEventEntry(
                (String) dbObject.get("aggregateIdentifier"),
                (Long) dbObject.get("sequenceNumber"),
                (byte[]) dbObject.get("serializedEvent"),
                (String) dbObject.get("timeStamp"),
                (String) dbObject.get("type")
        );
    }
}
