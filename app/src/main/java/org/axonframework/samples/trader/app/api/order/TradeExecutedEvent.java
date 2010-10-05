package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Allard Buijze
 */
public class TradeExecutedEvent extends DomainEvent {

    private final long tradeCount;
    private final int tradePrice;
    private final AggregateIdentifier buyOrderId;
    private final AggregateIdentifier sellOrderId;

    public TradeExecutedEvent(long tradeCount, int tradePrice, AggregateIdentifier buyOrderId, AggregateIdentifier sellOrderId) {
        this.tradeCount = tradeCount;
        this.tradePrice = tradePrice;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
    }

    public AggregateIdentifier getOrderBookIdentifier() {
        return getAggregateIdentifier();
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getTradePrice() {
        return tradePrice;
    }

    public AggregateIdentifier getBuyOrderId() {
        return buyOrderId;
    }

    public AggregateIdentifier getSellOrderId() {
        return sellOrderId;
    }
}
