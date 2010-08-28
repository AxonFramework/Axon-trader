package org.axonframework.samples.trader.app.query;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.OrderBookCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jettro Coenradie
 */
@Component
public class OrderBookListener {

    @PersistenceContext
    private EntityManager entityManager;

    private TradeItemRepository tradeItemRepository;

    @EventHandler
    public void handleOrderBookCreatedEvent(OrderBookCreatedEvent event) {
        OrderBookEntry entry = new OrderBookEntry();
        entry.setIdentifier(event.getOrderBookIdentifier());
        entry.setTradeItemIdentifier(event.getTradeItemIdentifier());
        entityManager.persist(entry);

        TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(event.getTradeItemIdentifier());
        tradeItemByIdentifier.setOrderBookIdentifier(event.getOrderBookIdentifier());
    }

    @Autowired
    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }

}
