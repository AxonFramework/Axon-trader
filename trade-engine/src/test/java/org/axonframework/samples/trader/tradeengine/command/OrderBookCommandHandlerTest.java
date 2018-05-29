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

package org.axonframework.samples.trader.tradeengine.command;

import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.OrderId;
import org.axonframework.samples.trader.api.orders.trades.*;
import org.axonframework.samples.trader.api.orders.transaction.TransactionId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class OrderBookCommandHandlerTest {

    private AggregateTestFixture<OrderBook> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(OrderBook.class);
        OrderBookCommandHandler commandHandler = new OrderBookCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testSimpleTradeExecution() {
        OrderId sellOrder = new OrderId();
        PortfolioId sellingUser = new PortfolioId();
        TransactionId sellingTransaction = new TransactionId();
        OrderBookId orderBookId = new OrderBookId();
        CreateSellOrderCommand orderCommand = new CreateSellOrderCommand(sellOrder,
                sellingUser,
                orderBookId,
                sellingTransaction,
                100,
                100);

        OrderId buyOrder = new OrderId();
        TransactionId buyTransactionId = new TransactionId();
        PortfolioId buyPortfolioId = new PortfolioId();
        fixture.given(new OrderBookCreatedEvent(orderBookId),
                new BuyOrderPlacedEvent(orderBookId, buyOrder, buyTransactionId, 200, 100, buyPortfolioId))
                .when(orderCommand)
                .expectEvents(new SellOrderPlacedEvent(orderBookId, sellOrder, sellingTransaction, 100, 100, sellingUser),
                        new TradeExecutedEvent(orderBookId,
                                100,
                                100,
                                buyOrder,
                                sellOrder,
                                buyTransactionId,
                                sellingTransaction));
    }

    @Test
    public void testMassiveSellerTradeExecution() {
        OrderId sellOrderId = new OrderId();
        OrderId buyOrder1 = new OrderId();
        OrderId buyOrder2 = new OrderId();
        OrderId buyOrder3 = new OrderId();
        TransactionId buyTransaction1 = new TransactionId();
        TransactionId buyTransaction2 = new TransactionId();
        TransactionId buyTransaction3 = new TransactionId();

        PortfolioId sellingUser = new PortfolioId();
        TransactionId sellingTransaction = new TransactionId();

        OrderBookId orderBookId = new OrderBookId();

        CreateSellOrderCommand sellOrder = new CreateSellOrderCommand(sellOrderId,
                sellingUser,
                orderBookId,
                sellingTransaction,
                200,
                100);
        fixture.given(new OrderBookCreatedEvent(orderBookId),
                new BuyOrderPlacedEvent(orderBookId, buyOrder1, buyTransaction1, 100, 100, new PortfolioId()),
                new BuyOrderPlacedEvent(orderBookId, buyOrder2, buyTransaction2, 66, 120, new PortfolioId()),
                new BuyOrderPlacedEvent(orderBookId, buyOrder3, buyTransaction3, 44, 140, new PortfolioId()))
                .when(sellOrder)
                .expectEvents(new SellOrderPlacedEvent(orderBookId, sellOrderId, sellingTransaction, 200, 100, sellingUser),
                        new TradeExecutedEvent(orderBookId,
                                44,
                                120,
                                buyOrder3,
                                sellOrderId,
                                buyTransaction3,
                                sellingTransaction),
                        new TradeExecutedEvent(orderBookId,
                                66,
                                110,
                                buyOrder2,
                                sellOrderId,
                                buyTransaction2,
                                sellingTransaction),
                        new TradeExecutedEvent(orderBookId,
                                90,
                                100,
                                buyOrder1,
                                sellOrderId,
                                buyTransaction1,
                                sellingTransaction));
    }

    @Test
    public void testCreateOrderBook() {
        OrderBookId orderBookId = new OrderBookId();
        CreateOrderBookCommand createOrderBookCommand = new CreateOrderBookCommand(orderBookId);
        fixture.given()
                .when(createOrderBookCommand)
                .expectEvents(new OrderBookCreatedEvent(orderBookId));
    }
}
