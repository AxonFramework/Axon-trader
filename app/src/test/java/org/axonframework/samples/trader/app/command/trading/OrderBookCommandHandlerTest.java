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

package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.AggregateIdentifierFactory;
import org.axonframework.samples.trader.app.api.order.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

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
        AggregateIdentifier buyOrder = AggregateIdentifierFactory.randomIdentifier();
        AggregateIdentifier sellingUser = AggregateIdentifierFactory.randomIdentifier();
        CreateSellOrderCommand orderCommand = new CreateSellOrderCommand(sellingUser,
                fixture.getAggregateIdentifier(),
                100,
                100);
        AggregateIdentifier sellOrder = orderCommand.getOrderId();
        fixture.given(new BuyOrderPlacedEvent(buyOrder, 200, 100, AggregateIdentifierFactory.randomIdentifier()))
                .when(orderCommand)
                .expectEvents(new SellOrderPlacedEvent(sellOrder, 100, 100, sellingUser),
                        new TradeExecutedEvent(100, 100, buyOrder, sellOrder));
    }

    @Test
    public void testMassiveSellerTradeExecution() {
        AggregateIdentifier buyOrder1 = AggregateIdentifierFactory.randomIdentifier();
        AggregateIdentifier buyOrder2 = AggregateIdentifierFactory.randomIdentifier();
        AggregateIdentifier buyOrder3 = AggregateIdentifierFactory.randomIdentifier();
        AggregateIdentifier sellingUser = AggregateIdentifierFactory.randomIdentifier();
        CreateSellOrderCommand sellOrder = new CreateSellOrderCommand(sellingUser,
                fixture.getAggregateIdentifier(),
                200,
                100);
        AggregateIdentifier sellOrderId = sellOrder.getOrderId();
        fixture.given(new BuyOrderPlacedEvent(buyOrder1, 100, 100, AggregateIdentifierFactory.randomIdentifier()),
                new BuyOrderPlacedEvent(buyOrder2, 66, 120, AggregateIdentifierFactory.randomIdentifier()),
                new BuyOrderPlacedEvent(buyOrder3, 44, 140, AggregateIdentifierFactory.randomIdentifier()))
                .when(sellOrder)
                .expectEvents(new SellOrderPlacedEvent(sellOrderId, 200, 100, sellingUser),
                        new TradeExecutedEvent(44, 120, buyOrder3, sellOrderId),
                        new TradeExecutedEvent(66, 110, buyOrder2, sellOrderId),
                        new TradeExecutedEvent(90, 100, buyOrder1, sellOrderId));
    }

    @Test
    public void testCreateOrderBook() {
        AggregateIdentifier tradeItemIdentifier = AggregateIdentifierFactory.randomIdentifier();
        CreateOrderBookCommand createOrderBookCommand = new CreateOrderBookCommand(tradeItemIdentifier);
        fixture.given()
                .when(createOrderBookCommand)
                .expectEvents(new OrderBookCreatedEvent(tradeItemIdentifier));
    }
}
