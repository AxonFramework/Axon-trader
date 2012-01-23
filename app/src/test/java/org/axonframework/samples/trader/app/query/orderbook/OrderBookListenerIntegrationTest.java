/*
 * Copyright (c) 2011. Gridshore
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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.app.api.order.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.app.api.order.OrderBookCreatedEvent;
import org.axonframework.samples.trader.app.api.order.SellOrderPlacedEvent;
import org.axonframework.samples.trader.app.api.order.TradeExecutedEvent;
import org.axonframework.samples.trader.app.query.company.CompanyEntry;
import org.axonframework.samples.trader.app.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.app.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.app.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.axonframework.test.utils.DomainEventUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Jettro Coenradie
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/persistence-infrastructure-context.xml"})
public class OrderBookListenerIntegrationTest {

    private OrderBookListener orderBookListener;

    @Autowired
    private OrderBookQueryRepository orderBookRepository;

    @Autowired
    private TradeExecutedQueryRepository tradeExecutedRepository;
    @Autowired
    private CompanyQueryRepository companyRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Before
    public void setUp() throws Exception {
        mongoTemplate.dropCollection(OrderBookEntry.class);
        mongoTemplate.dropCollection(CompanyEntry.class);
        mongoTemplate.dropCollection(TradeExecutedEntry.class);

        orderBookListener = new OrderBookListener();
        orderBookListener.setCompanyRepository(companyRepository);
        orderBookListener.setOrderBookRepository(orderBookRepository);
        orderBookListener.setTradeExecutedRepository(tradeExecutedRepository);
    }

    @Test
    public void testHandleOrderBookCreatedEvent() throws Exception {
        CompanyEntry company = createCompany();

        AggregateIdentifier orderBookIdentifier = new UUIDAggregateIdentifier();
        OrderBookCreatedEvent event = new OrderBookCreatedEvent(new UUIDAggregateIdentifier(company.getIdentifier()));
        DomainEventUtils.setAggregateIdentifier(event, orderBookIdentifier);

        orderBookListener.handleOrderBookCreatedEvent(event);
        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
    }

    @Test
    public void testHandleBuyOrderPlaced() throws Exception {
        CompanyEntry company = createCompany();
        OrderBookEntry orderBook = createOrderBook(company);

        AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier orderId = new UUIDAggregateIdentifier();
        AggregateIdentifier transactionId = new UUIDAggregateIdentifier();
        BuyOrderPlacedEvent event = new BuyOrderPlacedEvent(orderId, transactionId, 300, 100, userIdentifier);
        DomainEventUtils.setAggregateIdentifier(event, new UUIDAggregateIdentifier(orderBook.getIdentifier()));

        orderBookListener.handleBuyOrderPlaced(event);
        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
        assertEquals(1, orderBookEntry.buyOrders().size());
        assertEquals(300, orderBookEntry.buyOrders().get(0).getTradeCount());
    }

    @Test
    public void testHandleSellOrderPlaced() throws Exception {
        CompanyEntry company = createCompany();
        OrderBookEntry orderBook = createOrderBook(company);

        AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier orderId = new UUIDAggregateIdentifier();
        AggregateIdentifier transactionId = new UUIDAggregateIdentifier();
        SellOrderPlacedEvent event = new SellOrderPlacedEvent(orderId, transactionId, 300, 100, userIdentifier);
        DomainEventUtils.setAggregateIdentifier(event, new UUIDAggregateIdentifier(orderBook.getIdentifier()));

        orderBookListener.handleSellOrderPlaced(event);
        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
        assertEquals(1, orderBookEntry.sellOrders().size());
        assertEquals(300, orderBookEntry.sellOrders().get(0).getTradeCount());
    }

    @Test
    public void testHandleTradeExecuted() throws Exception {
        CompanyEntry company = createCompany();
        OrderBookEntry orderBook = createOrderBook(company);

        AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier sellOrderId = new UUIDAggregateIdentifier();
        AggregateIdentifier sellTransactionId = new UUIDAggregateIdentifier();
        SellOrderPlacedEvent sellOrderPlacedEvent = new SellOrderPlacedEvent(sellOrderId, sellTransactionId, 300, 100, userIdentifier);
        DomainEventUtils.setAggregateIdentifier(sellOrderPlacedEvent, new UUIDAggregateIdentifier(orderBook.getIdentifier()));

        orderBookListener.handleSellOrderPlaced(sellOrderPlacedEvent);

        AggregateIdentifier buyOrderId = new UUIDAggregateIdentifier();
        AggregateIdentifier buyTransactionId = new UUIDAggregateIdentifier();
        BuyOrderPlacedEvent buyOrderPlacedEvent = new BuyOrderPlacedEvent(buyOrderId, buyTransactionId, 300, 150, userIdentifier);
        DomainEventUtils.setAggregateIdentifier(buyOrderPlacedEvent, new UUIDAggregateIdentifier(orderBook.getIdentifier()));

        orderBookListener.handleBuyOrderPlaced(buyOrderPlacedEvent);

        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
        assertEquals(1, orderBookEntry.sellOrders().size());
        assertEquals(1, orderBookEntry.buyOrders().size());


        TradeExecutedEvent event = new TradeExecutedEvent(300, 125, buyOrderId, sellOrderId, buyTransactionId, sellTransactionId);
        DomainEventUtils.setAggregateIdentifier(event, new UUIDAggregateIdentifier(orderBook.getIdentifier()));
        orderBookListener.handleTradeExecuted(event);

        Iterable<TradeExecutedEntry> tradeExecutedEntries = tradeExecutedRepository.findAll();
        assertTrue(tradeExecutedEntries.iterator().hasNext());
        TradeExecutedEntry tradeExecutedEntry = tradeExecutedEntries.iterator().next();
        assertEquals("Test Company", tradeExecutedEntry.getCompanyName());
        assertEquals(300, tradeExecutedEntry.getTradeCount());
        assertEquals(125, tradeExecutedEntry.getTradePrice());
    }


    private OrderBookEntry createOrderBook(CompanyEntry company) {
        AggregateIdentifier orderBookIdentifier = new UUIDAggregateIdentifier();
        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setIdentifier(orderBookIdentifier.asString());
        orderBookEntry.setCompanyIdentifier(company.getIdentifier());
        orderBookEntry.setCompanyName(company.getName());
        orderBookRepository.save(orderBookEntry);
        return orderBookEntry;
    }

    private CompanyEntry createCompany() {
        AggregateIdentifier companyIdentifier = new UUIDAggregateIdentifier();
        CompanyEntry companyEntry = new CompanyEntry();
        companyEntry.setIdentifier(companyIdentifier.asString());
        companyEntry.setName("Test Company");
        companyEntry.setAmountOfShares(100000);
        companyEntry.setTradeStarted(true);
        companyEntry.setValue(1000);
        companyRepository.save(companyEntry);
        return companyEntry;
    }
}
