package org.axonframework.samples.trader.app.api.order;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderCommand {

    private UUID userId;
    private UUID orderBookId;
    private long tradeCount;
    private int itemPrice;
    private UUID orderId;

    protected AbstractOrderCommand(UUID userId, UUID orderBookId, long tradeCount, int itemPrice) {
        this.userId = userId;
        this.orderBookId = orderBookId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.orderId = UUID.randomUUID();
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getOrderBookId() {
        return orderBookId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
