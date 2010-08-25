package org.axonframework.samples.trader.app.api;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class CreateOrderBookCommand {
    private UUID tradeItemIdentifier;

    public CreateOrderBookCommand(UUID tradeItemIdentifier) {
        this.tradeItemIdentifier = tradeItemIdentifier;
    }

    public UUID getTradeItemIdentifier() {
        return tradeItemIdentifier;
    }
}
