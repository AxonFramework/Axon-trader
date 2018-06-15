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
import org.axonframework.samples.trader.api.orders.trades.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.CreateOrderBookCommand;
import org.axonframework.samples.trader.api.orders.trades.CreateSellOrderCommand;
import org.axonframework.samples.trader.api.orders.trades.OrderBookCreatedEvent;
import org.axonframework.samples.trader.api.orders.trades.SellOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.TransactionId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class OrderBookTest {

    private AggregateTestFixture<OrderBook> fixture;

    private OrderBookId orderBookId = new OrderBookId();
    private OrderBookCreatedEvent orderBookCreatedEvent = new OrderBookCreatedEvent(orderBookId);

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(OrderBook.class);
    }

    @Test
    public void testCreateOrderBook() {
        fixture.givenNoPriorActivity()
               .when(new CreateOrderBookCommand(orderBookId))
               .expectEvents(orderBookCreatedEvent);
    }

    @Test
    public void testSimpleTradeExecution() {
        OrderId sellOrder = new OrderId();
        PortfolioId sellingUser = new PortfolioId();
        TransactionId sellingTransaction = new TransactionId();

        OrderId buyOrder = new OrderId();
        TransactionId buyTransactionId = new TransactionId();
        PortfolioId buyPortfolioId = new PortfolioId();

        CreateSellOrderCommand orderCommand =
                new CreateSellOrderCommand(sellOrder, sellingUser, orderBookId, sellingTransaction, 100, 100);

        TradeExecutedEvent expectedTradeEvent =
                new TradeExecutedEvent(orderBookId, 100, 100, buyOrder, sellOrder, buyTransactionId, sellingTransaction);

        fixture.given(orderBookCreatedEvent,
                      new BuyOrderPlacedEvent(orderBookId, buyOrder, buyTransactionId, 200, 100, buyPortfolioId))
               .when(orderCommand)
               .expectEvents(new SellOrderPlacedEvent(orderBookId, sellOrder, sellingTransaction, 100, 100, sellingUser),
                             expectedTradeEvent);
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

        CreateSellOrderCommand sellOrder =
                new CreateSellOrderCommand(sellOrderId, sellingUser, orderBookId, sellingTransaction, 200, 100);

        TradeExecutedEvent expectedTradeEventOne =
                new TradeExecutedEvent(orderBookId, 44, 120, buyOrder3, sellOrderId, buyTransaction3, sellingTransaction);
        TradeExecutedEvent expectedTradeEventTwo =
                new TradeExecutedEvent(orderBookId, 66, 110, buyOrder2, sellOrderId, buyTransaction2, sellingTransaction);
        TradeExecutedEvent expectedTradeEventThree =
                new TradeExecutedEvent(orderBookId, 90, 100, buyOrder1, sellOrderId, buyTransaction1, sellingTransaction);

        fixture.given(orderBookCreatedEvent,
                      new BuyOrderPlacedEvent(orderBookId, buyOrder1, buyTransaction1, 100, 100, new PortfolioId()),
                      new BuyOrderPlacedEvent(orderBookId, buyOrder2, buyTransaction2, 66, 120, new PortfolioId()),
                      new BuyOrderPlacedEvent(orderBookId, buyOrder3, buyTransaction3, 44, 140, new PortfolioId()))
               .when(sellOrder)
               .expectEvents(new SellOrderPlacedEvent(orderBookId, sellOrderId, sellingTransaction, 200, 100, sellingUser),
                             expectedTradeEventOne,
                             expectedTradeEventTwo,
                             expectedTradeEventThree);
    }
}
