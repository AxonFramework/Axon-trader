package org.axonframework.samples.trader.app.query.tradeitem;

/**
 * @author Jettro Coenradie
 */
public class TradeItemEntry {
    private String identifier;
    private String orderBookIdentifier;
    private String name;
    private long value;
    private long amountOfShares;
    private boolean tradeStarted;

    public long getAmountOfShares() {
        return amountOfShares;
    }

    void setAmountOfShares(long amountOfShares) {
        this.amountOfShares = amountOfShares;
    }

    public String getIdentifier() {
        return identifier;
    }

    void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean isTradeStarted() {
        return tradeStarted;
    }

    void setTradeStarted(boolean tradeStarted) {
        this.tradeStarted = tradeStarted;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    void setOrderBookIdentifier(String orderBookIdentifier) {
        this.orderBookIdentifier = orderBookIdentifier;
    }
}
