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

import org.axonframework.samples.trader.orders.api.portfolio.money.MoneyReservedFromPortfolioEvent;
import org.axonframework.samples.trader.orders.api.portfolio.money.NotEnoughMoneyInPortfolioToMakeReservationEvent;
import org.axonframework.samples.trader.orders.api.transaction.*;
import org.axonframework.samples.trader.orders.command.matchers.*;
import org.axonframework.samples.trader.tradeengine.api.order.*;
import org.axonframework.test.saga.AnnotatedSagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.exactSequenceOf;

/**
 * @author Jettro Coenradie
 */
public class BuyTradeManagerSagaTest {

    private static final long TOTAL_ITEMS = 100;
    private static final long PRICE_PER_ITEM = 10;

    private TransactionId transactionIdentifier = new TransactionId();
    private OrderBookId orderbookIdentifier = new OrderBookId();
    private PortfolioId portfolioIdentifier = new PortfolioId();

    private AnnotatedSagaTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AnnotatedSagaTestFixture(BuyTradeManagerSaga.class);
    }

    @Test
    public void testHandle_SellTransactionStarted() throws Exception {
        fixture.givenAggregate(transactionIdentifier).published()
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ReserveMoneyFromPortfolioCommandMatcher(
                        portfolioIdentifier,
                        TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_MoneyIsReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .whenAggregate(portfolioIdentifier).publishes(new MoneyReservedFromPortfolioEvent(portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS
                        * PRICE_PER_ITEM))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ConfirmTransactionCommandMatcher(
                        transactionIdentifier)));
    }

    @Test
    public void testHandle_NotEnoughMoneyToReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .whenAggregate(portfolioIdentifier).publishes(new NotEnoughMoneyInPortfolioToMakeReservationEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .expectActiveSagas(0);
    }

    @Test
    public void testHandle_TransactionConfirmed() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionConfirmedEvent(transactionIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CreateBuyOrderCommandMatcher(portfolioIdentifier,
                        orderbookIdentifier,
                        TOTAL_ITEMS,
                        PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TransactionCancelled() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionCancelledEvent(transactionIdentifier, TOTAL_ITEMS, 0))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CancelMoneyReservationFromPortfolioCommandMatcher(
                        portfolioIdentifier,
                        TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TradeExecutedPlaced() {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();

        TransactionId sellTransactionIdentifier = new TransactionId();
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent(transactionIdentifier))
                .whenAggregate(orderbookIdentifier).publishes(new TradeExecutedEvent(orderbookIdentifier,
                TOTAL_ITEMS,
                99,
                buyOrderIdentifier,
                sellOrderIdentifier,
                transactionIdentifier,
                sellTransactionIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ExecutedTransactionCommandMatcher(TOTAL_ITEMS,
                        99,
                        transactionIdentifier),
                        andNoMore()));
    }

    @Test
    public void testHandle_BuyTransactionExecuted() {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent(transactionIdentifier))
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(orderbookIdentifier, TOTAL_ITEMS,
                99,
                buyOrderIdentifier,
                sellOrderIdentifier,
                transactionIdentifier,
                sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionExecutedEvent(transactionIdentifier, TOTAL_ITEMS, 99))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier,
                                        TOTAL_ITEMS * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier,
                                        orderbookIdentifier,
                                        TOTAL_ITEMS)));
    }

    @Test
    public void testHandle_BuyTransactionPartiallyExecuted() {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent(transactionIdentifier))
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(orderbookIdentifier,
                50,
                99,
                buyOrderIdentifier,
                sellOrderIdentifier,
                transactionIdentifier,
                sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionPartiallyExecutedEvent(transactionIdentifier, 50, 50, 99))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier, 50 * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier, orderbookIdentifier, 50)));
    }

    @Test
    public void testHandle_MultipleBuyTransactionPartiallyExecuted() {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent(transactionIdentifier))
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(orderbookIdentifier,
                50,
                99,
                buyOrderIdentifier,
                sellOrderIdentifier,
                transactionIdentifier,
                sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionPartiallyExecutedEvent(transactionIdentifier, 50, 50, 99))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier, 50 * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier, orderbookIdentifier, 50)));
    }
}
