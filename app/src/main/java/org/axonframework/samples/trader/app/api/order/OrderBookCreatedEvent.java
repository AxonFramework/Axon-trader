package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class OrderBookCreatedEvent extends DomainEvent {
    private AggregateIdentifier tradeItemIdentifier;

    public OrderBookCreatedEvent(AggregateIdentifier tradeItemIdentifier) {
        this.tradeItemIdentifier = tradeItemIdentifier;
    }

    public AggregateIdentifier getTradeItemIdentifier() {
        return tradeItemIdentifier;
    }

    public AggregateIdentifier getOrderBookIdentifier() {
        return getAggregateIdentifier();
    }
}
