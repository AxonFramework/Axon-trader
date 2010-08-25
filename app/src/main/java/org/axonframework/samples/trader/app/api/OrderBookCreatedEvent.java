package org.axonframework.samples.trader.app.api;

import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class OrderBookCreatedEvent extends DomainEvent {
    private UUID tradeItemIdentifier;

    public OrderBookCreatedEvent(UUID tradeItemIdentifier) {
        this.tradeItemIdentifier = tradeItemIdentifier;
    }

    public UUID getTradeItemIdentifier() {
        return tradeItemIdentifier;
    }

    public UUID getOrderBookIdentifier() {
        return getAggregateIdentifier();
    }
}
