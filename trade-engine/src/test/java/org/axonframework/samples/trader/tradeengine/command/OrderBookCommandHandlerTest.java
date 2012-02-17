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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.tradeengine.api.order.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.tradeengine.api.order.CreateOrderBookCommand;
import org.axonframework.samples.trader.tradeengine.api.order.CreateSellOrderCommand;
import org.axonframework.samples.trader.tradeengine.api.order.OrderBookCreatedEvent;
import org.axonframework.samples.trader.tradeengine.api.order.SellOrderPlacedEvent;
import org.axonframework.samples.trader.tradeengine.api.order.TradeExecutedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.*;

/**
 * @author Allard Buijze
 */
public class OrderBookCommandHandlerTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        OrderBookCommandHandler commandHandler = new OrderBookCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(OrderBook.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testSimpleTradeExecution() {
        AggregateIdentifier buyOrder = new UUIDAggregateIdentifier();
        AggregateIdentifier sellingUser = new UUIDAggregateIdentifier();
        AggregateIdentifier sellingTransaction = new UUIDAggregateIdentifier();
        CreateSellOrderCommand orderCommand = new CreateSellOrderCommand(sellingUser,
                                                                         fixture.getAggregateIdentifier(),
                                                                         sellingTransaction,
                                                                         100,
                                                                         100);
        AggregateIdentifier sellOrder = orderCommand.getOrderId();
        AggregateIdentifier buyTransactionId = new UUIDAggregateIdentifier();
        fixture.given(new BuyOrderPlacedEvent(buyOrder, buyTransactionId, 200, 100, new UUIDAggregateIdentifier()))
               .when(orderCommand)
               .expectEvents(new SellOrderPlacedEvent(sellOrder, sellingTransaction, 100, 100, sellingUser),
                             new TradeExecutedEvent(100,
                                                    100,
                                                    buyOrder,
                                                    sellOrder,
                                                    buyTransactionId,
                                                    sellingTransaction));
    }

    @Test
    public void testMassiveSellerTradeExecution() {
        AggregateIdentifier buyOrder1 = new UUIDAggregateIdentifier();
        AggregateIdentifier buyOrder2 = new UUIDAggregateIdentifier();
        AggregateIdentifier buyOrder3 = new UUIDAggregateIdentifier();
        AggregateIdentifier buyTransaction1 = new UUIDAggregateIdentifier();
        AggregateIdentifier buyTransaction2 = new UUIDAggregateIdentifier();
        AggregateIdentifier buyTransaction3 = new UUIDAggregateIdentifier();

        AggregateIdentifier sellingUser = new UUIDAggregateIdentifier();
        AggregateIdentifier sellingTransaction = new UUIDAggregateIdentifier();
        CreateSellOrderCommand sellOrder = new CreateSellOrderCommand(sellingUser,
                                                                      fixture.getAggregateIdentifier(),
                                                                      sellingTransaction,
                                                                      200,
                                                                      100);
        AggregateIdentifier sellOrderId = sellOrder.getOrderId();
        fixture.given(new BuyOrderPlacedEvent(buyOrder1, buyTransaction1, 100, 100, new UUIDAggregateIdentifier()),
                      new BuyOrderPlacedEvent(buyOrder2, buyTransaction2, 66, 120, new UUIDAggregateIdentifier()),
                      new BuyOrderPlacedEvent(buyOrder3, buyTransaction3, 44, 140, new UUIDAggregateIdentifier()))
               .when(sellOrder)
               .expectEvents(new SellOrderPlacedEvent(sellOrderId, sellingTransaction, 200, 100, sellingUser),
                             new TradeExecutedEvent(44,
                                                    120,
                                                    buyOrder3,
                                                    sellOrderId,
                                                    buyTransaction3,
                                                    sellingTransaction),
                             new TradeExecutedEvent(66,
                                                    110,
                                                    buyOrder2,
                                                    sellOrderId,
                                                    buyTransaction2,
                                                    sellingTransaction),
                             new TradeExecutedEvent(90,
                                                    100,
                                                    buyOrder1,
                                                    sellOrderId,
                                                    buyTransaction1,
                                                    sellingTransaction));
    }

    @Test
    public void testCreateOrderBook() {
        AggregateIdentifier companyIdentifier = new UUIDAggregateIdentifier();
        CreateOrderBookCommand createOrderBookCommand = new CreateOrderBookCommand(companyIdentifier);
        fixture.given()
               .when(createOrderBookCommand)
               .expectEvents(new OrderBookCreatedEvent(companyIdentifier));
    }
}
