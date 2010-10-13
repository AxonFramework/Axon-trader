/*
 * Copyright (c) 2010. Gridshore
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.app.query.tradeexecuted;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
