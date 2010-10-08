package org.axonframework.samples.trader.app.eventstore.mongo;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

import java.io.Serializable;

/**
 * @author Jettro Coenradie
 */
public class StubDomainEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 834667054977749990L;

    public StubDomainEvent() {
    }

    public StubDomainEvent(long sequenceNumber) {
        setSequenceNumber(sequenceNumber);
    }

    public StubDomainEvent(AggregateIdentifier aggregateIdentifier) {
        setAggregateIdentifier(aggregateIdentifier);
    }

    public StubDomainEvent(AggregateIdentifier aggregateIdentifier, long sequenceNumber) {
        setAggregateIdentifier(aggregateIdentifier);
        setSequenceNumber(sequenceNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StubDomainEvent aggregate [");
        sb.append(getAggregateIdentifier());
        sb.append("] sequenceNo [");
        sb.append(getSequenceNumber());
        sb.append("]");
        return sb.toString();
    }
}
