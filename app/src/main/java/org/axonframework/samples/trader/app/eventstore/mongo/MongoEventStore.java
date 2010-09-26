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
 * <p>This is an implementation of the <code>SnapshotEventStore</code> based on a MongoDB instance. Since the events
 * must be persisted, we use the highest possible WriteConcern. For a test environment with only one node, this is
 * the SAFE WriteConcern. In a production environment with a Mongo Replica set, the REPLICAS_SAFE WriteConcern is used.
 * </p>
 * <p>Whether to initialize a test environment or a production environment is decided based on the provided testContext
 * parameter. If passed true, the test environment is setup. If passed false, the production environment is assumed</p>
 *
 * @author Jettro Coenradie
 */
public class MongoEventStore implements SnapshotEventStore, EventStoreManagement {
    private static final Logger logger = LoggerFactory.getLogger(MongoEventStore.class);

    private static final String AGGREGATE_IDENTIFIER = "aggregateIdentifier";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String TIME_STAMP = "timeStamp";
    private static final String TYPE = "type";
    private static final String SERIALIZED_EVENT = "serializedEvent";

    private static final int EVENT_VISITOR_BATCH_SIZE = 50;
    private final MongoHelper mongo;
    private final EventSerializer eventSerializer;
    private final WriteConcern writeConcern;

    /**
     * Constructor that accepts an EventSerializer, the MongoHelper and a string containing the testContext. The
     * TestContext can be Null. Provide true in case of the test context.
     *
     * @param eventSerializer Your own EventSerializer
     * @param mongo MongoHelper to obtain the database and the collections.
     * @param testContext String containing true or anything else
     */
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

    /**
     * Constructor that uses the default EventSerializer.
     *
     * @param mongo MongoHelper to obtain the database and the collections.
     * @param testContext String containing true or anything else
     */
    public MongoEventStore(MongoHelper mongo, String testContext) {
        this(new XStreamEventSerializer(), mongo, testContext);
    }

    @Override
    public void appendEvents(String type, DomainEventStream events) {
        List<DBObject> entries = new ArrayList<DBObject>();
        while (events.hasNext()) {
            DomainEvent event = events.next();
            DomainEventEntry entry = new DomainEventEntry(type, event, eventSerializer);
            DBObject mongoEntry = createMongoEventEntry(entry);
            entries.add(mongoEntry);
        }
        mongo.domainEvents().insert(entries.toArray(new DBObject[entries.size()]), writeConcern);

        if (logger.isDebugEnabled()) {
            logger.debug("{} events of type {} appended", new Object[]{entries.size(), type});
        }
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

    @Override
    public void appendSnapshotEvent(String type, DomainEvent snapshotEvent) {
        SnapshotEventEntry snapshotEventEntry = new SnapshotEventEntry(type, snapshotEvent, eventSerializer);
        DBObject mongoSnapshotEntry = BasicDBObjectBuilder.start()
                .add(AGGREGATE_IDENTIFIER, snapshotEventEntry.getAggregateIdentifier().toString())
                .add(SEQUENCE_NUMBER, snapshotEventEntry.getSequenceNumber())
                .add(SERIALIZED_EVENT, snapshotEventEntry.getSerializedEvent())
                .add(TIME_STAMP, snapshotEventEntry.getTimeStamp())
                .add(TYPE, snapshotEventEntry.getType())
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

    private DBObject createMongoEventEntry(DomainEventEntry entry) {
        return BasicDBObjectBuilder.start()
                .add(AGGREGATE_IDENTIFIER, entry.getAggregateIdentifier().toString())
                .add(SEQUENCE_NUMBER, entry.getSequenceNumber())
                .add(TIME_STAMP, entry.getTimeStamp().toString())
                .add(TYPE, entry.getType())
                .add(SERIALIZED_EVENT, entry.getSerializedEvent())
                .get();
    }

    private List<DomainEvent> readEventSegmentInternal(String type, UUID identifier, long firstSequenceNumber) {
        DBObject mongoEntry = BasicDBObjectBuilder.start()
                .add(AGGREGATE_IDENTIFIER, identifier.toString())
                .add(SEQUENCE_NUMBER, firstSequenceNumber)
                .add(TYPE, type)
                .get();

        DBCursor dbCursor = mongo.domainEvents().find(mongoEntry);
        List<DomainEvent> events = new ArrayList<DomainEvent>(dbCursor.size());
        while (dbCursor.hasNext()) {
            events.add(eventSerializer.deserialize((byte[]) dbCursor.next().get(SERIALIZED_EVENT)));
        }
        return events;
    }


    private SnapshotEventEntry loadLastSnapshotEvent(String type, UUID identifier) {
        DBObject mongoEntry = BasicDBObjectBuilder.start()
                .add(AGGREGATE_IDENTIFIER, identifier.toString())
                .add(TYPE, type)
                .get();
        DBCursor dbCursor = mongo.domainEvents().find(mongoEntry).sort(new BasicDBObject(SEQUENCE_NUMBER, -1));

        if (!dbCursor.hasNext()) {
            return null;
        }
        DBObject first = dbCursor.next();
        return new SnapshotEventEntry(
                (String) first.get(AGGREGATE_IDENTIFIER),
                (Long) first.get(SEQUENCE_NUMBER),
                (byte[]) first.get(SERIALIZED_EVENT),
                (String) first.get(TIME_STAMP),
                (String) first.get(TYPE)
        );
    }

    private List<DomainEventEntry> fetchBatch(int startPosition, int batchSize) {
        DBObject sort = BasicDBObjectBuilder.start()
                .add(TIME_STAMP, -1)
                .add(SEQUENCE_NUMBER, -1)
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
                (String) dbObject.get(AGGREGATE_IDENTIFIER),
                (Long) dbObject.get(SEQUENCE_NUMBER),
                (byte[]) dbObject.get(SERIALIZED_EVENT),
                (String) dbObject.get(TIME_STAMP),
                (String) dbObject.get(TYPE)
        );
    }
}
