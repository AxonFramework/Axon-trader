package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Allard Buijze
 */
public class SellOrderPlacedEvent extends AbstractOrderPlacedEvent {

    public SellOrderPlacedEvent(AggregateIdentifier orderId, long tradeCount, int itemPrice, AggregateIdentifier userId) {
        super(orderId, tradeCount, itemPrice, userId);
    }
}
