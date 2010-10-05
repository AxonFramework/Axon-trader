package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Allard Buijze
 */
public class CreateSellOrderCommand extends AbstractOrderCommand {

    public CreateSellOrderCommand(AggregateIdentifier userId, AggregateIdentifier orderBookId, long tradeCount, int itemPrice) {
        super(userId, orderBookId, tradeCount, itemPrice);
    }
}
