package org.axonframework.samples.trader.app.query.orderbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Repository
public class OrderBookRepositoryMongo implements OrderBookRepository {
    private MongoHelper mongo;

    @Override
    public List<OrderBookEntry> listAllOrderBooks() {
        DBCursor dbCursor = mongo.orderBooks().find();

        List<OrderBookEntry> entries = new ArrayList<OrderBookEntry>();
        while (dbCursor.hasNext()) {
            DBObject next = dbCursor.next();
            OrderBookEntry entry = mapOrderBookEntryFromMongo(next);
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public OrderBookEntry findByIdentifier(String aggregateIdentifier) {
        DBObject one = mongo.orderBooks().findOne(new BasicDBObject("identifier", aggregateIdentifier));
        return mapOrderBookEntryFromMongo(one);
    }

    @Override
    public OrderBookEntry findByTradeItem(String tradeItemIdentifier) {
        DBObject one = mongo.orderBooks().findOne(new BasicDBObject("tradeItemIdentifier", tradeItemIdentifier));
        return mapOrderBookEntryFromMongo(one);
    }

    @Override
    public OrderEntry findByOrderIdentifier(String orderIdentifier) {
        throw new UnsupportedOperationException();
    }

    private OrderBookEntry mapOrderBookEntryFromMongo(DBObject next) {
        OrderBookEntry entry = new OrderBookEntry();
        entry.setTradeItemName((String) next.get("tradeItemName"));
        entry.setIdentifier((String) next.get("identifier"));
        entry.setTradeItemIdentifier((String) next.get("tradeItemIdentifier"));
        if (next.containsField("sellOrders")) {
            List<DBObject> sellOrderObjects = (List<DBObject>) next.get("sellOrders");
            for (DBObject sellOrderObject : sellOrderObjects) {
                entry.sellOrders().add(mapOrderEntryFromMongo(sellOrderObject));
            }
        }
        if (next.containsField("buyOrders")) {
            List<DBObject> buyOrderObjects = (List<DBObject>) next.get("buyOrders");
            for (DBObject buyOrderObject : buyOrderObjects) {
                entry.buyOrders().add(mapOrderEntryFromMongo(buyOrderObject));
            }
        }
        return entry;
    }

    private OrderEntry mapOrderEntryFromMongo(DBObject orderObject) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setType((String) orderObject.get("type"));
        orderEntry.setItemsRemaining((Long) orderObject.get("itemsRemaining"));
        orderEntry.setTradeCount((Long) orderObject.get("tradeCount"));
        orderEntry.setUserId(UUID.fromString((String) orderObject.get("userId")));
        orderEntry.setIdentifier(UUID.fromString((String) orderObject.get("identifier")));
        orderEntry.setItemPrice((Integer) orderObject.get("itemPrice"));
        return orderEntry;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
