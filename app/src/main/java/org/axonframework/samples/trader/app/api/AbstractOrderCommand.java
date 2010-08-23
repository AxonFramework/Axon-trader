package org.axonframework.samples.trader.app.api;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderCommand {

    private UUID userId;
    private UUID tradeItemId;
    private long tradeCount;
    private int itemPrice;
    private UUID orderId;

    protected AbstractOrderCommand(UUID userId, UUID tradeItemId, long tradeCount, int itemPrice) {
        this.userId = userId;
        this.tradeItemId = tradeItemId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.orderId = UUID.randomUUID();
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTradeItemId() {
        return tradeItemId;
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
