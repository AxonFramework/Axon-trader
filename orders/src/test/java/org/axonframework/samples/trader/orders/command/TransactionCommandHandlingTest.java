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

package org.axonframework.samples.trader.orders.command;

import org.axonframework.samples.trader.orders.api.transaction.*;
import org.axonframework.samples.trader.tradeengine.api.order.OrderBookId;
import org.axonframework.samples.trader.tradeengine.api.order.PortfolioId;
import org.axonframework.samples.trader.tradeengine.api.order.TransactionId;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jettro Coenradie
 */
public class TransactionCommandHandlingTest {

    private FixtureConfiguration fixture;
    OrderBookId orderBook = new OrderBookId();
    PortfolioId portfolio = new PortfolioId();
    TransactionId transactionId = new TransactionId();

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture(Transaction.class);
        TransactionCommandHandler commandHandler = new TransactionCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testStartBuyTransaction() {
        StartBuyTransactionCommand command = new StartBuyTransactionCommand(orderBook, portfolio, 200, 20);
        fixture.given()
                .when(command)
                .expectEvents(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20));
    }

    @Test
    public void testStartSellTransaction() {
        StartSellTransactionCommand command = new StartSellTransactionCommand(orderBook, portfolio, 200, 20);
        fixture.given()
                .when(command)
                .expectEvents(new SellTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20));
    }

    @Test
    public void testConfirmTransaction() {
        ConfirmTransactionCommand command = new ConfirmTransactionCommand(transactionId);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20))
                .when(command)
                .expectEvents(new BuyTransactionConfirmedEvent(transactionId));
    }

    @Test
    public void testCancelTransaction() {
        CancelTransactionCommand command = new CancelTransactionCommand(transactionId);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20))
                .when(command)
                .expectEvents(new BuyTransactionCancelledEvent(transactionId, 200, 0));
    }

    @Test
    public void testCancelTransaction_partiallyExecuted() {
        CancelTransactionCommand command = new CancelTransactionCommand(transactionId);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20),
                new BuyTransactionPartiallyExecutedEvent(transactionId, 100, 100, 20))
                .when(command)
                .expectEvents(new BuyTransactionCancelledEvent(transactionId, 200, 100));
    }

    @Test
    public void testExecuteTransaction() {
        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionId, 200, 20);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent(transactionId))
                .when(command)
                .expectEvents(new BuyTransactionExecutedEvent(transactionId, 200, 20));
    }

    @Test
    public void testExecuteTransaction_partiallyExecuted() {
        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionId, 50, 20);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent(transactionId))
                .when(command)
                .expectEvents(new BuyTransactionPartiallyExecutedEvent(transactionId, 50, 50, 20));
    }

    @Test
    public void testExecuteTransaction_completeAfterPartiallyExecuted() {
        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionId, 150, 20);
        fixture.given(new BuyTransactionStartedEvent(transactionId, orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent(transactionId),
                new BuyTransactionPartiallyExecutedEvent(transactionId, 50, 50, 20)
        )
                .when(command)
                .expectEvents(new BuyTransactionExecutedEvent(transactionId, 150, 20));
        // TODO moeten we nu ook nog een partially executed event gooien?
    }
}
