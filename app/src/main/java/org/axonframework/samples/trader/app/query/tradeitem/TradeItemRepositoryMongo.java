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

package org.axonframework.samples.trader.app.query.tradeitem;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Component
public class TradeItemRepositoryMongo implements TradeItemRepository {
    private MongoHelper mongo;

    @Override
    public List<TradeItemEntry> listAllTradeItems() {
        DBCursor tradeItemsCursor = mongo.tradeItems().find();
        List<TradeItemEntry> tradeItems = new ArrayList<TradeItemEntry>();
        while (tradeItemsCursor.hasNext()) {
            DBObject nextTradeItem = tradeItemsCursor.next();
            TradeItemEntry tradeItemEntry = mapTradeItemFromMongo(nextTradeItem);
            tradeItems.add(tradeItemEntry);
        }
        return tradeItems;
    }

    @Override
    public TradeItemEntry findTradeItemByIdentifier(String tradeItemIdentifier) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", tradeItemIdentifier).get();
        return mapTradeItemFromMongo(mongo.tradeItems().findOne(query));
    }

    @Override
    public TradeItemEntry findTradeItemByOrderBookIdentifier(String orderBookIdentifier) {
        DBObject query = BasicDBObjectBuilder.start().add("orderBookIdentifier", orderBookIdentifier).get();
        return mapTradeItemFromMongo(mongo.tradeItems().findOne(query));
    }

    /**
     * Used to create a TradeItemEntry object based on a mongo DBObject
     *
     * @param mongoTradeItemObject The mongo object to create a TradeItemEntry
     * @return TradeItemEntry created from te mongo object
     */
    private TradeItemEntry mapTradeItemFromMongo(DBObject mongoTradeItemObject) {
        TradeItemEntry tradeItemEntry = new TradeItemEntry();
        tradeItemEntry.setIdentifier((String) mongoTradeItemObject.get("identifier"));
        tradeItemEntry.setName((String) mongoTradeItemObject.get("name"));
        tradeItemEntry.setValue((Long) mongoTradeItemObject.get("value"));
        tradeItemEntry.setAmountOfShares((Long) mongoTradeItemObject.get("amountOfShares"));
        tradeItemEntry.setTradeStarted((Boolean) mongoTradeItemObject.get("tradeStarted"));
        if (mongoTradeItemObject.containsField("orderBookIdentifier")) {
            String orderBookIdentifier = (String) mongoTradeItemObject.get("orderBookIdentifier");
            tradeItemEntry.setOrderBookIdentifier(orderBookIdentifier);
        }
        return tradeItemEntry;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
