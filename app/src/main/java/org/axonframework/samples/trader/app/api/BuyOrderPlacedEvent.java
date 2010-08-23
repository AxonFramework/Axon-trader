package org.axonframework.samples.trader.app.api;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class BuyOrderPlacedEvent extends AbstractOrderPlacedEvent {

    public BuyOrderPlacedEvent(UUID orderId, long tradeCount, int itemPrice, UUID userId) {
        super(orderId, tradeCount, itemPrice, userId);
    }

}
