package org.axonframework.samples.trader.webui.order;

/**
 * @author Jettro Coenradie
 */
public class AbstractOrder {
    private String tradeItemId;
    private long tradeCount;
    private int itemPrice;

    public AbstractOrder() {
    }

    public AbstractOrder(int itemPrice, long tradeCount, String tradeItemId) {
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.tradeItemId = tradeItemId;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }
}
