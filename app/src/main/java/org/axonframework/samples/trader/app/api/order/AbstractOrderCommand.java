package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.AggregateIdentifierFactory;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderCommand {

    private AggregateIdentifier userId;
    private AggregateIdentifier orderBookId;
    private long tradeCount;
    private int itemPrice;
    private AggregateIdentifier orderId;

    protected AbstractOrderCommand(AggregateIdentifier userId, AggregateIdentifier orderBookId, long tradeCount, int itemPrice) {
        this.userId = userId;
        this.orderBookId = orderBookId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.orderId = AggregateIdentifierFactory.fromUUID( UUID.randomUUID());
    }

    public AggregateIdentifier getUserId() {
        return userId;
    }

    public AggregateIdentifier getOrderBookId() {
        return orderBookId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }
}
