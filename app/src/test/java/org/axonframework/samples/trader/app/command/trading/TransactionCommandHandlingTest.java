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

package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.app.api.transaction.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jettro Coenradie
 */
public class TransactionCommandHandlingTest {
    private FixtureConfiguration fixture;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        TransactionCommandHandler commandHandler = new TransactionCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(Transaction.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testStartBuyTransaction() {
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();
        StartBuyTransactionCommand command = new StartBuyTransactionCommand(orderBook, portfolio, 200, 20);
        fixture.given()
                .when(command)
                .expectEvents(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20));
    }

    @Test
    public void testStartSellTransaction() {
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();
        StartSellTransactionCommand command = new StartSellTransactionCommand(orderBook, portfolio, 200, 20);
        fixture.given()
                .when(command)
                .expectEvents(new SellTransactionStartedEvent(orderBook, portfolio, 200, 20));
    }

    @Test
    public void testConfirmTransaction() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        ConfirmTransactionCommand command = new ConfirmTransactionCommand(transactionIdentifier);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20))
                .when(command)
                .expectEvents(new BuyTransactionConfirmedEvent());
    }

    @Test
    public void testCancelTransaction() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        CancelTransactionCommand command = new CancelTransactionCommand(transactionIdentifier);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20))
                .when(command)
                .expectEvents(new BuyTransactionCancelledEvent(200, 0));
    }

    @Test
    public void testCancelTransaction_partiallyExecuted() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        CancelTransactionCommand command = new CancelTransactionCommand(transactionIdentifier);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20),
                new BuyTransactionPartiallyExecutedEvent(100, 100, 20))
                .when(command)
                .expectEvents(new BuyTransactionCancelledEvent(200, 100));
    }

    @Test
    public void testExecuteTransaction() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionIdentifier, 200, 20);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent())
                .when(command)
                .expectEvents(new BuyTransactionExecutedEvent(200, 20));
    }

    @Test
    public void testExecuteTransaction_partiallyExecuted() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionIdentifier, 50, 20);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent())
                .when(command)
                .expectEvents(new BuyTransactionPartiallyExecutedEvent(50, 50, 20));
    }

    @Test
    public void testExecuteTransaction_completeAfterPartiallyExecuted() {
        AggregateIdentifier transactionIdentifier = fixture.getAggregateIdentifier();
        AggregateIdentifier orderBook = new UUIDAggregateIdentifier();
        AggregateIdentifier portfolio = new UUIDAggregateIdentifier();

        ExecutedTransactionCommand command = new ExecutedTransactionCommand(transactionIdentifier, 150, 20);
        fixture.given(new BuyTransactionStartedEvent(orderBook, portfolio, 200, 20),
                new BuyTransactionConfirmedEvent(),
                new BuyTransactionPartiallyExecutedEvent(50, 50, 20)
        )
                .when(command)
                .expectEvents(new BuyTransactionExecutedEvent(150, 20));
        // TODO moeten we nu ook nog een partially executed event gooien?
    }

}
