package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.tradeitem.TradeItemCreatedEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class TradeItem extends AbstractAnnotatedAggregateRoot {

    public TradeItem(AggregateIdentifier identifier, String name, long value, long amountOfShares) {
        super(identifier);
        apply(new TradeItemCreatedEvent(name,value,amountOfShares));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public TradeItem(AggregateIdentifier identifier) {
        super(identifier);
    }

    @EventHandler
    public void onTradeItemCreated(TradeItemCreatedEvent event) {
        // nothing for now
    }
}
