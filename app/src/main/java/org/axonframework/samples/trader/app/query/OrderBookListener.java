package org.axonframework.samples.trader.app.query;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.app.api.OrderBookCreatedEvent;
import org.axonframework.samples.trader.app.api.SellOrderPlacedEvent;
import org.axonframework.samples.trader.app.api.TradeExecutedEvent;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Component
public class OrderBookListener {
    private MongoHelper mongo;

    @PersistenceContext
    private EntityManager entityManager;

    private TradeItemRepository tradeItemRepository;
    private OrderBookRepository orderBookRepository;

    @EventHandler
    public void handleOrderBookCreatedEvent(OrderBookCreatedEvent event) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", event.getTradeItemIdentifier().toString()).get();
        DBObject tradeItem = mongo.tradeItems().findOne(query);
        tradeItem.put("orderBookIdentifier",event.getOrderBookIdentifier().toString());
        mongo.tradeItems().update(query,tradeItem);

        OrderBookEntry entry = new OrderBookEntry();
        entry.setIdentifier(event.getOrderBookIdentifier());
        entry.setTradeItemIdentifier(event.getTradeItemIdentifier());
        entry.setTradeItemName((String) tradeItem.get("name"));
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

    @EventHandler
    public void handleTradeExecuted(TradeExecutedEvent event) {
        UUID buyOrderId = event.getBuyOrderId();
        UUID sellOrderId = event.getSellOrderId();

        UUID orderBookIdentifier = event.getOrderBookIdentifier();
        TradeItemEntry tradeItem = tradeItemRepository.findTradeItemByOrderBookIdentifier(orderBookIdentifier);

        TradeExecutedEntry entry = new TradeExecutedEntry();
        entry.setTradeCount(event.getTradeCount());
        entry.setTradePrice(event.getTradePrice());
        entry.setTradeItemName(tradeItem.getName());
        entry.setOrderBookIdentifier(orderBookIdentifier);
        entityManager.persist(entry);

        OrderEntry buyOrderEntry = orderBookRepository.findByOrderIdentifier(buyOrderId);
        buyOrderEntry.setItemsRemaining(buyOrderEntry.getItemsRemaining() - event.getTradeCount());

        OrderEntry sellOrderEntry = orderBookRepository.findByOrderIdentifier(sellOrderId);
        sellOrderEntry.setItemsRemaining(sellOrderEntry.getItemsRemaining() - event.getTradeCount());
    }

    @Autowired
    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }

    @Autowired
    public void setOrderBookRepository(OrderBookRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
