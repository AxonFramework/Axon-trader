package org.axonframework.samples.trader.app.api;

import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class TradeExecutedEvent extends DomainEvent {

    private final long tradeCount;
    private final int tradePrice;
    private final UUID buyOrderId;
    private final UUID sellOrderId;

    public TradeExecutedEvent(long tradeCount, int tradePrice, UUID buyOrderId, UUID sellOrderId) {
        this.tradeCount = tradeCount;
        this.tradePrice = tradePrice;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
    }

    public UUID getOrderBookIdentifier() {
        return getAggregateIdentifier();
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getTradePrice() {
        return tradePrice;
    }

    public UUID getBuyOrderId() {
        return buyOrderId;
    }

    public UUID getSellOrderId() {
        return sellOrderId;
    }
}
