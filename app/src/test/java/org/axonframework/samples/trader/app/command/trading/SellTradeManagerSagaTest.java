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
import org.axonframework.samples.trader.app.api.order.TradeExecutedEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemsReservedEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.NotEnoughItemsAvailableToReserveInPortfolio;
import org.axonframework.samples.trader.app.api.transaction.*;
import org.axonframework.samples.trader.app.command.trading.matchers.*;
import org.axonframework.test.saga.AnnotatedSagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.exactSequenceOf;

/**
 * @author Jettro Coenradie
 */
public class SellTradeManagerSagaTest {
    private AggregateIdentifier transactionIdentifier = new UUIDAggregateIdentifier();
    private AggregateIdentifier orderbookIdentifier = new UUIDAggregateIdentifier();
    private AggregateIdentifier portfolioIdentifier = new UUIDAggregateIdentifier();

    private AnnotatedSagaTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AnnotatedSagaTestFixture(SellTradeManagerSaga.class);
    }

    @Test
    public void testHandle_SellTransactionStarted() throws Exception {
        fixture.givenAggregate(transactionIdentifier).published()
                .whenAggregate(transactionIdentifier).publishes(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .expectAssociationWith("orderBookIdentifier", orderbookIdentifier)
                .expectAssociationWith("portfolioIdentifier", portfolioIdentifier)
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ReservedItemsCommandMatcher(orderbookIdentifier.asString(), portfolioIdentifier.asString(), 100)));
    }

    @Test
    public void testHandle_ItemsReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .whenAggregate(portfolioIdentifier).publishes(new ItemsReservedEvent(orderbookIdentifier, 100))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ConfirmTransactionCommandMatcher(transactionIdentifier)));
    }

    @Test
    public void testHandle_TransactionConfirmed() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .andThenAggregate(portfolioIdentifier).published(new ItemsReservedEvent(orderbookIdentifier, 100))
                .whenAggregate(transactionIdentifier).publishes(new SellTransactionConfirmedEvent())
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CreateSellOrderCommandMatcher(portfolioIdentifier, orderbookIdentifier, 100, 10)));
    }


    @Test
    public void testHandle_NotEnoughItemsToReserve() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .whenAggregate(portfolioIdentifier).publishes(new NotEnoughItemsAvailableToReserveInPortfolio(orderbookIdentifier, 50, 100))
                .expectActiveSagas(0);
    }

    @Test
    public void testHandle_TransactionCancelled() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .whenAggregate(transactionIdentifier).publishes(new SellTransactionCancelledEvent(50, 0))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CancelItemReservationForPortfolioCommandMatcher(orderbookIdentifier, portfolioIdentifier, 50)));
    }

    @Test
    public void testHandle_TradeExecutedPlaced() {
        AggregateIdentifier buyOrderIdentifier = new UUIDAggregateIdentifier();
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 99))
                .andThenAggregate(portfolioIdentifier).published(new ItemsReservedEvent(orderbookIdentifier, 100))
                .andThenAggregate(transactionIdentifier).published(new SellTransactionConfirmedEvent())
                .whenAggregate(orderbookIdentifier).publishes(new TradeExecutedEvent(100, 102, buyOrderIdentifier, orderbookIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ExecutedTransactionCommandMatcher(100, 102, transactionIdentifier), andNoMore()));

    }

    @Test
    public void testHandle_SellTransactionExecuted() {
        AggregateIdentifier buyOrderIdentifier = new UUIDAggregateIdentifier();
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 99))
                .andThenAggregate(portfolioIdentifier).published(new ItemsReservedEvent(orderbookIdentifier, 100))
                .andThenAggregate(transactionIdentifier).published(new SellTransactionConfirmedEvent())
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(100, 102, buyOrderIdentifier, orderbookIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new SellTransactionExecutedEvent(100, 102))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier, portfolioIdentifier, 100),
                                new DepositMoneyToPortfolioCommandMatcher(portfolioIdentifier, 100 * 102)));
    }

    @Test
    public void testHandle_SellTransactionPartiallyExecuted() {
        AggregateIdentifier buyOrderIdentifier = new UUIDAggregateIdentifier();
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 99))
                .andThenAggregate(portfolioIdentifier).published(new ItemsReservedEvent(orderbookIdentifier, 100))
                .andThenAggregate(transactionIdentifier).published(new SellTransactionConfirmedEvent())
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(100, 102, buyOrderIdentifier, orderbookIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new SellTransactionPartiallyExecutedEvent(50, 75, 102))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier, portfolioIdentifier, 50),
                                new DepositMoneyToPortfolioCommandMatcher(portfolioIdentifier, 50 * 102)));
    }
}
