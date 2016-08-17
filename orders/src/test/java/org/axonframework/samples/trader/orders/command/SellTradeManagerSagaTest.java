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

import org.axonframework.samples.trader.api.portfolio.stock.ItemsReservedEvent;
import org.axonframework.samples.trader.api.portfolio.stock.NotEnoughItemsAvailableToReserveInPortfolio;
import org.axonframework.samples.trader.api.orders.transaction.*;
import org.axonframework.samples.trader.api.orders.trades.OrderBookId;
import org.axonframework.samples.trader.api.orders.trades.OrderId;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.api.orders.trades.TransactionId;
import org.axonframework.samples.trader.orders.command.matchers.*;
import org.axonframework.test.saga.AnnotatedSagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.exactSequenceOf;

/**
 * @author Jettro Coenradie
 */
public class SellTradeManagerSagaTest {

    private TransactionId transactionIdentifier = new TransactionId();
    private OrderBookId orderbookIdentifier = new OrderBookId();
    private PortfolioId portfolioIdentifier = new PortfolioId();

    private AnnotatedSagaTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AnnotatedSagaTestFixture(SellTradeManagerSaga.class);
    }

    @Test
    public void testHandle_SellTransactionStarted() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published()
                .whenAggregate(transactionIdentifier.toString()).publishes(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                10))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ReservedItemsCommandMatcher(orderbookIdentifier,
                        portfolioIdentifier,
                        100)));
    }

    @Test
    public void testHandle_ItemsReserved() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                10))
                .whenAggregate(portfolioIdentifier.toString()).publishes(new ItemsReservedEvent(portfolioIdentifier, orderbookIdentifier,
                transactionIdentifier,
                100))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ConfirmTransactionCommandMatcher(
                        transactionIdentifier)));
    }

    @Test
    public void testHandle_TransactionConfirmed() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                10))
                .andThenAggregate(portfolioIdentifier.toString()).published(new ItemsReservedEvent(portfolioIdentifier, orderbookIdentifier,
                transactionIdentifier,
                100))
                .whenAggregate(transactionIdentifier.toString()).publishes(new SellTransactionConfirmedEvent(transactionIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CreateSellOrderCommandMatcher(portfolioIdentifier,
                        orderbookIdentifier,
                        100,
                        10)));
    }


    @Test
    public void testHandle_NotEnoughItemsToReserve() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                10))
                .whenAggregate(portfolioIdentifier.toString()).publishes(new NotEnoughItemsAvailableToReserveInPortfolio(
                portfolioIdentifier,
                orderbookIdentifier,
                transactionIdentifier,
                50,
                100))
                .expectActiveSagas(0);
    }

    @Test
    public void testHandle_TransactionCancelled() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                10))
                .whenAggregate(transactionIdentifier.toString()).publishes(new SellTransactionCancelledEvent(transactionIdentifier, 50, 0))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CancelItemReservationForPortfolioCommandMatcher(
                        orderbookIdentifier,
                        portfolioIdentifier,
                        50)));
    }

    @Test
    public void testHandle_TradeExecutedPlaced() throws Exception {
        OrderId buyOrderIdentifier = new OrderId();
        OrderId sellOrderIdentifier = new OrderId();
        TransactionId buyTransactionIdentifier = new TransactionId();
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                99))
                .andThenAggregate(portfolioIdentifier.toString()).published(new ItemsReservedEvent(portfolioIdentifier, orderbookIdentifier,
                transactionIdentifier,
                100))
                .andThenAggregate(transactionIdentifier.toString()).published(new SellTransactionConfirmedEvent(transactionIdentifier))
                .whenAggregate(orderbookIdentifier.toString()).publishes(new TradeExecutedEvent(orderbookIdentifier,
                100,
                102,
                buyOrderIdentifier,
                sellOrderIdentifier,
                buyTransactionIdentifier,
                transactionIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ExecutedTransactionCommandMatcher(100,
                        102,
                        transactionIdentifier),
                        andNoMore()));
    }

    @Test
    public void testHandle_SellTransactionExecuted() throws Exception {
        OrderId buyOrderIdentifier = new OrderId();
        OrderId sellOrderIdentifier = new OrderId();
        TransactionId buyTransactionIdentifier = new TransactionId();
        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                99))
                .andThenAggregate(portfolioIdentifier.toString()).published(new ItemsReservedEvent(portfolioIdentifier, orderbookIdentifier,
                transactionIdentifier,
                100))
                .andThenAggregate(transactionIdentifier.toString()).published(new SellTransactionConfirmedEvent(transactionIdentifier))
                .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                100,
                102,
                buyOrderIdentifier,
                sellOrderIdentifier,
                buyTransactionIdentifier,
                transactionIdentifier))
                .whenAggregate(transactionIdentifier.toString()).publishes(new SellTransactionExecutedEvent(transactionIdentifier, 100, 102))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier,
                                        portfolioIdentifier,
                                        100),
                                new DepositMoneyToPortfolioCommandMatcher(portfolioIdentifier, 100 * 102)));
    }

    @Test
    public void testHandle_SellTransactionPartiallyExecuted() throws Exception {
        OrderId buyOrderIdentifier = new OrderId();
        OrderId sellOrderIdentifier = new OrderId();
        TransactionId buyTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new SellTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                100,
                99))
                .andThenAggregate(portfolioIdentifier.toString()).published(new ItemsReservedEvent(portfolioIdentifier, orderbookIdentifier,
                transactionIdentifier,
                100))
                .andThenAggregate(transactionIdentifier.toString()).published(new SellTransactionConfirmedEvent(transactionIdentifier))
                .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                100,
                102,
                buyOrderIdentifier,
                sellOrderIdentifier,
                buyTransactionIdentifier,
                transactionIdentifier))
                .whenAggregate(transactionIdentifier.toString()).publishes(new SellTransactionPartiallyExecutedEvent(transactionIdentifier, 50, 75, 102))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier,
                                        portfolioIdentifier,
                                        50),
                                new DepositMoneyToPortfolioCommandMatcher(portfolioIdentifier, 50 * 102)));
    }
}
