package org.axonframework.samples.trader.app.query.orderbook;

/**
 * @author Jettro Coenradie
 */
public class OrderEntry {
    private String identifier;
    private OrderBookEntry orderBookEntry;
    private long tradeCount;
    private int itemPrice;
    private String userId;
    private long itemsRemaining;
    private String type;

    public String getIdentifier() {
        return identifier;
    }

    void setIdentifier(String identifier) {
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

    public String getUserId() {
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }
}
