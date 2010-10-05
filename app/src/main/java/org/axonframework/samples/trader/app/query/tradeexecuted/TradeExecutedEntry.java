package org.axonframework.samples.trader.app.query.tradeexecuted;

/**
 * @author Jettro Coenradie
 */
public class TradeExecutedEntry {
    private long tradeCount;
    private int tradePrice;
    private String tradeItemName;
    private String orderBookIdentifier;

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    public void setTradeItemName(String tradeItemName) {
        this.tradeItemName = tradeItemName;
    }

    public int getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(int tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    public void setOrderBookIdentifier(String orderBookIdentifier) {
        this.orderBookIdentifier = orderBookIdentifier;
    }
}
