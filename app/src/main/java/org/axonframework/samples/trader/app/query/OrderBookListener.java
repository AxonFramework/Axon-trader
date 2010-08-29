package org.axonframework.samples.trader.app.query;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.app.api.OrderBookCreatedEvent;
import org.axonframework.samples.trader.app.api.SellOrderPlacedEvent;
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
    private OrderBookRepository orderBookRepository;

    @EventHandler
    public void handleOrderBookCreatedEvent(OrderBookCreatedEvent event) {
        TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(event.getTradeItemIdentifier());
        tradeItemByIdentifier.setOrderBookIdentifier(event.getOrderBookIdentifier());
        entityManager.merge(tradeItemByIdentifier);

        OrderBookEntry entry = new OrderBookEntry();
        entry.setIdentifier(event.getOrderBookIdentifier());
        entry.setTradeItemIdentifier(event.getTradeItemIdentifier());
        entry.setTradeItemName(tradeItemByIdentifier.getName());
        entityManager.persist(entry);
    }

    @EventHandler
    public void handleBuyOrderPlaced(BuyOrderPlacedEvent event) {
        OrderBookEntry orderBook = orderBookRepository.findByIdentifier(event.getAggregateIdentifier());

        OrderEntry entry = new OrderEntry();
        entry.setIdentifier(event.getOrderId());
        entry.setItemPrice(event.getItemPrice());
        entry.setItemsRemaining(event.getTradeCount());
        entry.setTradeCount(event.getTradeCount());
        entry.setUserId(event.getUserId());
        entry.setOrderBookEntry(orderBook);
        entry.setType("Buy");
        entityManager.persist(entry);
    }

    @EventHandler
    public void handleSellOrderPlaced(SellOrderPlacedEvent event) {
        OrderBookEntry orderBook = orderBookRepository.findByIdentifier(event.getAggregateIdentifier());

        OrderEntry entry = new OrderEntry();
        entry.setIdentifier(event.getOrderId());
        entry.setItemPrice(event.getItemPrice());
        entry.setItemsRemaining(event.getTradeCount());
        entry.setTradeCount(event.getTradeCount());
        entry.setUserId(event.getUserId());
        entry.setOrderBookEntry(orderBook);
        entry.setType("Sell");
        entityManager.persist(entry);
    }

    @Autowired
    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }

    @Autowired
    public void setOrderBookRepository(OrderBookRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }
}
