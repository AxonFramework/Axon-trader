package org.axonframework.samples.trader.app.api.order;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class CreateSellOrderCommand extends AbstractOrderCommand {

    public CreateSellOrderCommand(UUID userId, UUID orderBookId, long tradeCount, int itemPrice) {
        super(userId, orderBookId, tradeCount, itemPrice);
    }
}
