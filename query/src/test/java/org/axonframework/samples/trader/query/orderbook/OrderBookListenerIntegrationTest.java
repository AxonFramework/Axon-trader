/*
 * Copyright (c) 2010-2012. Axon Framework
 *
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

package org.axonframework.samples.trader.query.orderbook;

import org.axonframework.samples.trader.api.company.CompanyCreatedEvent;
import org.axonframework.samples.trader.api.company.CompanyId;
import org.axonframework.samples.trader.api.company.OrderBookAddedToCompanyEvent;
import org.axonframework.samples.trader.api.orders.trades.*;
import org.axonframework.samples.trader.query.company.CompanyEntry;
import org.axonframework.samples.trader.query.company.CompanyListener;
import org.axonframework.samples.trader.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Jettro Coenradie
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/integration-test-context.xml")
@ActiveProfiles("hsqldb")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class OrderBookListenerIntegrationTest {

    OrderId orderId = new OrderId();
    PortfolioId portfolioId = new PortfolioId();
    TransactionId transactionId = new TransactionId();
    OrderBookId orderBookId = new OrderBookId();
    CompanyId companyId = new CompanyId();
    private OrderBookListener orderBookListener;
    @Autowired
    private OrderBookQueryRepository orderBookRepository;
    @Autowired
    private TradeExecutedQueryRepository tradeExecutedRepository;
    @Autowired
    private CompanyQueryRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        orderBookRepository.deleteAll();
        companyRepository.deleteAll();
        tradeExecutedRepository.deleteAll();

        CompanyListener companyListener = new CompanyListener();
        companyListener.setCompanyRepository(companyRepository);
        companyListener.handleCompanyCreatedEvent(new CompanyCreatedEvent(companyId, "Test Company", 100, 100));

        orderBookListener = new OrderBookListener();
        orderBookListener.setCompanyRepository(companyRepository);
        orderBookListener.setOrderBookRepository(orderBookRepository);
        orderBookListener.setTradeExecutedRepository(tradeExecutedRepository);
    }

    @Test
    public void testHandleOrderBookCreatedEvent() throws Exception {
        OrderBookAddedToCompanyEvent event = new OrderBookAddedToCompanyEvent(companyId, orderBookId);

        orderBookListener.handleOrderBookAddedToCompanyEvent(event);
        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
    }

    @Test
    public void testHandleBuyOrderPlaced() throws Exception {
        CompanyEntry company = createCompany();
        OrderBookEntry orderBook = createOrderBook(company);

        BuyOrderPlacedEvent event = new BuyOrderPlacedEvent(orderBookId, orderId, transactionId, 300, 100, portfolioId);

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

        OrderBookId orderBookId = new OrderBookId(orderBook.getIdentifier());
        SellOrderPlacedEvent event = new SellOrderPlacedEvent(orderBookId, orderId, transactionId, 300, 100, portfolioId);

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

        OrderId sellOrderId = new OrderId();
        TransactionId sellTransactionId = new TransactionId();
        SellOrderPlacedEvent sellOrderPlacedEvent = new SellOrderPlacedEvent(orderBookId,
                sellOrderId,
                sellTransactionId,
                400,
                100,
                portfolioId);

        orderBookListener.handleSellOrderPlaced(sellOrderPlacedEvent);

        OrderId buyOrderId = new OrderId();
        TransactionId buyTransactionId = new TransactionId();
        BuyOrderPlacedEvent buyOrderPlacedEvent = new BuyOrderPlacedEvent(orderBookId
                , buyOrderId,
                buyTransactionId,
                300,
                150,
                portfolioId);

        orderBookListener.handleBuyOrderPlaced(buyOrderPlacedEvent);

        Iterable<OrderBookEntry> all = orderBookRepository.findAll();
        OrderBookEntry orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
        assertEquals(1, orderBookEntry.sellOrders().size());
        assertEquals(1, orderBookEntry.buyOrders().size());


        TradeExecutedEvent event = new TradeExecutedEvent(orderBookId,
                300,
                125,
                buyOrderId,
                sellOrderId,
                buyTransactionId,
                sellTransactionId);
        orderBookListener.handleTradeExecuted(event);

        Iterable<TradeExecutedEntry> tradeExecutedEntries = tradeExecutedRepository.findAll();
        assertTrue(tradeExecutedEntries.iterator().hasNext());
        TradeExecutedEntry tradeExecutedEntry = tradeExecutedEntries.iterator().next();
        assertEquals("Test Company", tradeExecutedEntry.getCompanyName());
        assertEquals(300, tradeExecutedEntry.getTradeCount());
        assertEquals(125, tradeExecutedEntry.getTradePrice());

        all = orderBookRepository.findAll();
        orderBookEntry = all.iterator().next();
        assertNotNull("The first item of the iterator for orderbooks should not be null", orderBookEntry);
        assertEquals("Test Company", orderBookEntry.getCompanyName());
        assertEquals(1, orderBookEntry.sellOrders().size());
        assertEquals(0, orderBookEntry.buyOrders().size());
    }


    private OrderBookEntry createOrderBook(CompanyEntry company) {
        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setIdentifier(orderBookId.toString());
        orderBookEntry.setCompanyIdentifier(company.getIdentifier());
        orderBookEntry.setCompanyName(company.getName());
        orderBookRepository.save(orderBookEntry);
        return orderBookEntry;
    }

    private CompanyEntry createCompany() {
        CompanyId companyId = new CompanyId();
        CompanyEntry companyEntry = new CompanyEntry();
        companyEntry.setIdentifier(companyId.toString());
        companyEntry.setName("Test Company");
        companyEntry.setAmountOfShares(100000);
        companyEntry.setTradeStarted(true);
        companyEntry.setValue(1000);
        companyRepository.save(companyEntry);
        return companyEntry;
    }
}
