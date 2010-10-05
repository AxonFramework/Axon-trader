package org.axonframework.samples.trader.app.query.tradeexecuted;

import com.mongodb.BasicDBObjectBuilder;
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
public class TradeExecutedRepositoryMongo implements TradeExecutedRepository {
    private MongoHelper mongo;

    @Override
    public List<TradeExecutedEntry> findExecutedTradesForOrderBook(String orderBookIdentifier) {
        DBObject query = BasicDBObjectBuilder.start().add("orderBookIdentifier", orderBookIdentifier).get();
        DBCursor tradesExecutedCursor = mongo.tradesExecuted().find(query);
        List<TradeExecutedEntry> tradesExecuted = new ArrayList<TradeExecutedEntry>();
        while (tradesExecutedCursor.hasNext()) {
            DBObject tradeExecutedMongo = tradesExecutedCursor.next();
            tradesExecuted.add(mapFromMongoToTradeExecutedEntry(tradeExecutedMongo));
        }
        return tradesExecuted;
    }

    private TradeExecutedEntry mapFromMongoToTradeExecutedEntry(DBObject tradeExecutedMongo) {
        TradeExecutedEntry entry = new TradeExecutedEntry();
        entry.setOrderBookIdentifier((String) tradeExecutedMongo.get("orderBookIdentifier"));
        entry.setTradeCount((Long) tradeExecutedMongo.get("count"));
        entry.setTradeItemName((String) tradeExecutedMongo.get("name"));
        entry.setTradePrice((Integer) tradeExecutedMongo.get("price"));
        return entry;
    }


    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
