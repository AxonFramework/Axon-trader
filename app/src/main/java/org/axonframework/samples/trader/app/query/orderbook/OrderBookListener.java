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

package org.axonframework.samples.trader.app.query.orderbook;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.order.*;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.axonframework.samples.trader.app.query.company.CompanyEntry;
import org.axonframework.samples.trader.app.query.company.CompanyRepository;
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

    private CompanyRepository companyRepository;

    @EventHandler
    public void handleOrderBookCreatedEvent(OrderBookCreatedEvent event) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", event.getCompanyIdentifier().asString()).get();
        DBObject company = mongo.companies().findOne(query);
        company.put("orderBookIdentifier", event.getOrderBookIdentifier().toString());
        mongo.companies().update(query, company);

        DBObject orderBook = BasicDBObjectBuilder.start()
                .add("identifier", event.getOrderBookIdentifier().toString())
                .add("companyIdentifier", event.getCompanyIdentifier().toString())
                .add("companyName", company.get("name")).get();
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
        CompanyEntry company = companyRepository.findCompanyByOrderBookIdentifier(orderBookIdentifier.asString());
        DBObject tradeExecutedMongo = BasicDBObjectBuilder.start()
                .add("count", event.getTradeCount())
                .add("price", event.getTradePrice())
                .add("name", company.getName())
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
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}
