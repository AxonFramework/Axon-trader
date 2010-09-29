package org.axonframework.samples.trader.app.api.order;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class SellOrderPlacedEvent extends AbstractOrderPlacedEvent {

    public SellOrderPlacedEvent(UUID orderId, long tradeCount, int itemPrice, UUID userId) {
        super(orderId, tradeCount, itemPrice, userId);
    }
}
