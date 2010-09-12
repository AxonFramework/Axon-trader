package org.axonframework.samples.trader.app.query.tradeexecuted;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class TradeExecutedEntry {
    private long tradeCount;
    private int tradePrice;
    private String tradeItemName;
    private UUID orderBookIdentifier;

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

    public UUID getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    public void setOrderBookIdentifier(UUID orderBookIdentifier) {
        this.orderBookIdentifier = orderBookIdentifier;
    }
}
