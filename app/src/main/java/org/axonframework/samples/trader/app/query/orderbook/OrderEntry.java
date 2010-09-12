package org.axonframework.samples.trader.app.query.orderbook;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class OrderEntry {
    private UUID identifier;
    private OrderBookEntry orderBookEntry;
    private long tradeCount;
    private int itemPrice;
    private UUID userId;
    private long itemsRemaining;
    private String type;

    public UUID getIdentifier() {
        return identifier;
    }

    void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    void setItemsRemaining(long itemsRemaining) {
        this.itemsRemaining = itemsRemaining;
    }

    public OrderBookEntry getOrderBookEntry() {
        return orderBookEntry;
    }

    void setOrderBookEntry(OrderBookEntry orderBookEntry) {
        this.orderBookEntry = orderBookEntry;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public UUID getUserId() {
        return userId;
    }

    void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }
}
