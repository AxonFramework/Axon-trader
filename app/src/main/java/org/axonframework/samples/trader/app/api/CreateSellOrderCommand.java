package org.axonframework.samples.trader.app.api;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class CreateSellOrderCommand extends AbstractOrderCommand {

    public CreateSellOrderCommand(UUID userId, UUID tradeItemId, long tradeCount, int itemPrice) {
        super(userId, tradeItemId, tradeCount, itemPrice);
    }
}
