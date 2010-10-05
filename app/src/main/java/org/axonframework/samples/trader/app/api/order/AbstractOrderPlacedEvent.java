package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderPlacedEvent extends DomainEvent {

    private final AggregateIdentifier orderId;
    private final long tradeCount;
    private final int itemPrice;
    private final AggregateIdentifier userId;

    protected AbstractOrderPlacedEvent(AggregateIdentifier orderId, long tradeCount, int itemPrice, AggregateIdentifier userId) {
        this.orderId = orderId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.userId = userId;
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public AggregateIdentifier getUserId() {
        return userId;
    }

}
