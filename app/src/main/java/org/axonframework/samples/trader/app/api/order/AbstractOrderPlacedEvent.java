package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderPlacedEvent extends DomainEvent {

    private final UUID orderId;
    private final long tradeCount;
    private final int itemPrice;
    private final UUID userId;

    protected AbstractOrderPlacedEvent(UUID orderId, long tradeCount, int itemPrice, UUID userId) {
        this.orderId = orderId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.userId = userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public UUID getUserId() {
        return userId;
    }

}
