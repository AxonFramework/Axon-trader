package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Jettro Coenradie
 */
public class CreateOrderBookCommand {
    private AggregateIdentifier tradeItemIdentifier;

    public CreateOrderBookCommand(AggregateIdentifier tradeItemIdentifier) {
        this.tradeItemIdentifier = tradeItemIdentifier;
    }

    public AggregateIdentifier getTradeItemIdentifier() {
        return tradeItemIdentifier;
    }
}
