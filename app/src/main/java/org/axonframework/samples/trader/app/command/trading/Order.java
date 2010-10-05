package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Allard Buijze
 */
class Order {

    private final int itemPrice;
    private final long tradeCount;
    private final AggregateIdentifier userId;
    private long itemsRemaining;
    private AggregateIdentifier orderId;

    public Order(AggregateIdentifier orderId, int itemPrice, long tradeCount, AggregateIdentifier userId) {
        this.orderId = orderId;
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.itemsRemaining = tradeCount;
        this.userId = userId;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public AggregateIdentifier getUserId() {
        return userId;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }

    public void recordTraded(long tradeCount) {
        this.itemsRemaining -= tradeCount;
    }
}
