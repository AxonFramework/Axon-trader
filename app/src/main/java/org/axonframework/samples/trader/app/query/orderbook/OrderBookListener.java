package org.axonframework.samples.trader.app.query.orderbook;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.order.*;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemEntry;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Component
public class OrderBookListener {
    private static final String BUY = "Buy";
    private static final String SELL = "Sell";
    private MongoHelper mongo;

    private TradeItemRepository tradeItemRepository;

    @EventHandler
    public void handleOrderBookCreatedEvent(OrderBookCreatedEvent event) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", event.getTradeItemIdentifier().asString()).get();
        DBObject tradeItem = mongo.tradeItems().findOne(query);
        tradeItem.put("orderBookIdentifier", event.getOrderBookIdentifier().toString());
        mongo.tradeItems().update(query, tradeItem);

        DBObject orderBook = BasicDBObjectBuilder.start()
                .add("identifier", event.getOrderBookIdentifier().toString())
                .add("tradeItemIdentifier", event.getTradeItemIdentifier().toString())
                .add("tradeItemName", tradeItem.get("name")).get();
        mongo.orderBooks().insert(orderBook);
    }

    @EventHandler
    public void handleBuyOrderPlaced(BuyOrderPlacedEvent event) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", event.getAggregateIdentifier().asString()).get();
        DBObject orderBook = mongo.orderBooks().findOne(query);

        DBObject buyOrder = createPlacedOrder(event, BUY);

        if (!orderBook.containsField("buyOrders")) {
            orderBook.put("buyOrders", new ArrayList());
        }

        ((List) orderBook.get("buyOrders")).add(buyOrder);

        mongo.orderBooks().update(query, orderBook);
    }

    @EventHandler
    public void handleSellOrderPlaced(SellOrderPlacedEvent event) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", event.getAggregateIdentifier().asString()).get();
        DBObject orderBook = mongo.orderBooks().findOne(query);

        DBObject buyOrder = createPlacedOrder(event, SELL);

        if (!orderBook.containsField("sellOrders")) {
            orderBook.put("sellOrders", new ArrayList());
        }

        ((List) orderBook.get("sellOrders")).add(buyOrder);

        mongo.orderBooks().update(query, orderBook);
    }

    @EventHandler
    public void handleTradeExecuted(TradeExecutedEvent event) {
        AggregateIdentifier buyOrderId = event.getBuyOrderId();
        AggregateIdentifier sellOrderId = event.getSellOrderId();

        AggregateIdentifier orderBookIdentifier = event.getOrderBookIdentifier();
        TradeItemEntry tradeItem = tradeItemRepository.findTradeItemByOrderBookIdentifier(orderBookIdentifier.asString());
        DBObject tradeExecutedMongo = BasicDBObjectBuilder.start()
                .add("count", event.getTradeCount())
                .add("price", event.getTradePrice())
                .add("name", tradeItem.getName())
                .add("orderBookIdentifier", orderBookIdentifier.asString())
                .get();

        mongo.tradesExecuted().insert(tradeExecutedMongo);

        // TODO find a better solution or maybe pull them apart
        DBObject query = BasicDBObjectBuilder.start()
                .add("identifier", event.getAggregateIdentifier().asString())
                .get();
        DBObject orderBook = mongo.orderBooks().findOne(query);
        List buyOrders = (List) orderBook.get("buyOrders");
        for (Object orderObj : buyOrders) {
            DBObject order = (DBObject) orderObj;
            if (((String) order.get("identifier")).equals(buyOrderId.asString())) {
                long itemsRemaining = (Long) order.get("itemsRemaining");
                order.put("itemsRemaining", itemsRemaining - event.getTradeCount());
                break;
            }
        }

        List sellOrders = (List) orderBook.get("sellOrders");
        for (Object orderObj : sellOrders) {
            DBObject order = (DBObject) orderObj;
            if (((String) order.get("identifier")).equals(sellOrderId.asString())) {
                long itemsRemaining = (Long) order.get("itemsRemaining");
                order.put("itemsRemaining", itemsRemaining - event.getTradeCount());
                break;
            }
        }
        mongo.orderBooks().update(query, orderBook);
    }

    private DBObject createPlacedOrder(AbstractOrderPlacedEvent event, String type) {
        return BasicDBObjectBuilder.start()
                .add("identifier", event.getOrderId().asString())
                .add("itemPrice", event.getItemPrice())
                .add("itemsRemaining", event.getTradeCount())
                .add("tradeCount", event.getTradeCount())
                .add("userId", event.getUserId().asString())
                .add("type", type).get();
    }

    @Autowired
    public void setTradeItemRepository(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
