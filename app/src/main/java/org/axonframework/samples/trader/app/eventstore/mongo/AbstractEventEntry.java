package org.axonframework.samples.trader.app.eventstore.mongo;

import org.axonframework.domain.DomainEvent;
import org.axonframework.eventstore.EventSerializer;
import org.joda.time.LocalDateTime;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class AbstractEventEntry {
    private String aggregateIdentifier;
    private long sequenceNumber;
    private String timeStamp;
    private String type;
    private byte[] serializedEvent;

    protected AbstractEventEntry(String type, DomainEvent event, EventSerializer eventSerializer) {
        this.type = type;
        this.aggregateIdentifier = event.getAggregateIdentifier().toString();
        this.sequenceNumber = event.getSequenceNumber();
        this.serializedEvent = eventSerializer.serialize(event);
        this.timeStamp = event.getTimestamp().toString();
    }

    protected AbstractEventEntry(String aggregateIdentifier, long sequenceNumber, byte[] serializedEvent, String timeStamp, String type) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.sequenceNumber = sequenceNumber;
        this.serializedEvent = serializedEvent;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public DomainEvent getDomainEvent(EventSerializer eventSerializer) {
        return eventSerializer.deserialize(serializedEvent);
    }

    public UUID getAggregateIdentifier() {
        return UUID.fromString(aggregateIdentifier);
    }

    public String getType() {
        return type;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public LocalDateTime getTimeStamp() {
        return new LocalDateTime(timeStamp);
    }

    protected byte[] getSerializedEvent() {
        return serializedEvent;
    }
}
