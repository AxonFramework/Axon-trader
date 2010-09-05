package org.axonframework.samples.trader.webui.order;

import javax.validation.constraints.Min;

/**
 * @author Jettro Coenradie
 */
public class AbstractOrder {
    private String tradeItemId;
    private String tradeItemName;

    @Min(0)
    private long tradeCount;

    @Min(0)
    private int itemPrice;

    public AbstractOrder() {
    }

    public AbstractOrder(int itemPrice, long tradeCount, String tradeItemId, String tradeItemName) {
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.tradeItemId = tradeItemId;
        this.tradeItemName = tradeItemName;
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

    public String getTradeItemName() {
        return tradeItemName;
    }

    public void setTradeItemName(String tradeItemName) {
        this.tradeItemName = tradeItemName;
    }
}
