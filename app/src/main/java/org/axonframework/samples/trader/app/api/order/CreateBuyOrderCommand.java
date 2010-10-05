package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Allard Buijze
 */
public class CreateBuyOrderCommand extends AbstractOrderCommand {

    public CreateBuyOrderCommand(AggregateIdentifier userId, AggregateIdentifier tradeItemId, long tradeCount, int itemPrice) {
        super(userId, tradeItemId, tradeCount, itemPrice);
    }
}
