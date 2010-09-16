package org.axonframework.samples.trader.app.eventstore.mongo;

import org.axonframework.domain.DomainEvent;
import org.axonframework.eventstore.EventSerializer;

/**
 * @author Jettro Coenradie
 */
public class DomainEventEntry extends AbstractEventEntry {

    public DomainEventEntry(String type, DomainEvent event, EventSerializer eventSerializer) {
        super(type, event, eventSerializer);
    }

    public DomainEventEntry(String aggregateIdentifier, long sequenceNumber, byte[] serializedEvent, String timeStamp, String type) {
        super(aggregateIdentifier, sequenceNumber, serializedEvent, timeStamp, type);
    }
}
