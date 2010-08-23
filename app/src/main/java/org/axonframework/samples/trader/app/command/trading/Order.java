package org.axonframework.samples.trader.app.command.trading;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
class Order {

    private final int itemPrice;
    private final long tradeCount;
    private final UUID userId;
    private long itemsRemaining;
    private UUID orderId;

    public Order(UUID orderId, int itemPrice, long tradeCount, UUID userId) {
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

    public UUID getUserId() {
        return userId;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void recordTraded(long tradeCount) {
        this.itemsRemaining -= tradeCount;
    }
}
