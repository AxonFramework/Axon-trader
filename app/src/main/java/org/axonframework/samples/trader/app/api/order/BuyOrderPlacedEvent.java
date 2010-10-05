package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class BuyOrderPlacedEvent extends AbstractOrderPlacedEvent {

    public BuyOrderPlacedEvent(AggregateIdentifier orderId, long tradeCount, int itemPrice, AggregateIdentifier userId) {
        super(orderId, tradeCount, itemPrice, userId);
    }

}
