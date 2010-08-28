package org.axonframework.samples.trader.app.query;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.tradeitem.TradeItemCreatedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jettro Coenradie
 */
@Component
public class TradeItemListener {
    @PersistenceContext
    private EntityManager entityManager;

    @EventHandler
    public void handleTradeItemCreatedEvent(TradeItemCreatedEvent event) {
        TradeItemEntry entry = new TradeItemEntry();
        entry.setIdentifier(event.getTradeItemIdentifier());
        entry.setName(event.getTradeItemName());
        entry.setValue(event.getTradeItemValue());
        entry.setAmountOfShares(event.getAmountOfShares());
        entry.setTradeStarted(true);
        entityManager.persist(entry);
    }

}
