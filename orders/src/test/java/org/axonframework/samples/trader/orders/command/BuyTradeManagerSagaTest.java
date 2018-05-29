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

import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.OrderId;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.*;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.cash.CashReservationRejectedEvent;
import org.axonframework.samples.trader.api.portfolio.cash.CashReservedEvent;
import org.axonframework.samples.trader.orders.command.matchers.*;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.exactSequenceOf;

public class BuyTradeManagerSagaTest {

    private static final long TOTAL_ITEMS = 100;
    private static final long PRICE_PER_ITEM = 10;

    private TransactionId transactionIdentifier = new TransactionId();
    private OrderBookId orderbookIdentifier = new OrderBookId();
    private PortfolioId portfolioIdentifier = new PortfolioId();

    private SagaTestFixture<BuyTradeManagerSaga> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new SagaTestFixture<>(BuyTradeManagerSaga.class);
    }

    @Test
    public void testHandle_SellTransactionStarted() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published()
               .whenAggregate(transactionIdentifier.toString()).publishes(
                new BuyTransactionStartedEvent(transactionIdentifier,
                                               orderbookIdentifier,
                                               portfolioIdentifier,
                                               TOTAL_ITEMS,
                                               PRICE_PER_ITEM))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(ReserveMoneyFromPortfolioCommandMatcher.newInstance(
                               portfolioIdentifier,
                               TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_MoneyIsReserved() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(
                new BuyTransactionStartedEvent(transactionIdentifier,
                                               orderbookIdentifier,
                                               portfolioIdentifier,
                                               TOTAL_ITEMS,
                                               PRICE_PER_ITEM))
               .whenAggregate(portfolioIdentifier.toString()).publishes(
                new CashReservedEvent(portfolioIdentifier, transactionIdentifier,
                                      TOTAL_ITEMS
                                              * PRICE_PER_ITEM))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(ConfirmTransactionCommandMatcher.newInstance(
                               transactionIdentifier)));
    }

    @Test
    public void testHandle_NotEnoughMoneyToReserved() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(
                new BuyTransactionStartedEvent(transactionIdentifier,
                                               orderbookIdentifier,
                                               portfolioIdentifier,
                                               TOTAL_ITEMS,
                                               PRICE_PER_ITEM))
               .whenAggregate(portfolioIdentifier.toString()).publishes(
                new CashReservationRejectedEvent(
                        portfolioIdentifier, transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
               .expectActiveSagas(0);
    }

    @Test
    public void testHandle_TransactionConfirmed() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(
                new BuyTransactionStartedEvent(transactionIdentifier,
                                               orderbookIdentifier,
                                               portfolioIdentifier,
                                               TOTAL_ITEMS,
                                               PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(
                new CashReservedEvent(
                        portfolioIdentifier,
                        transactionIdentifier,
                        TOTAL_ITEMS * PRICE_PER_ITEM))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionConfirmedEvent(
                transactionIdentifier))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(exactSequenceOf(
                       CreateBuyOrderCommandMatcher.newInstance(portfolioIdentifier,
                                                                orderbookIdentifier,
                                                                TOTAL_ITEMS,
                                                                PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TransactionCancelled() throws Exception {
        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionCancelledEvent(
                transactionIdentifier,
                TOTAL_ITEMS,
                0))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(exactSequenceOf(CancelMoneyReservationFromPortfolioCommandMatcher
                                                                         .newInstance(
                                                                                 portfolioIdentifier,
                                                                                 TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TradeExecutedPlaced() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();

        TransactionId sellTransactionIdentifier = new TransactionId();
        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString()).published(new BuyTransactionConfirmedEvent(
                transactionIdentifier))
               .whenAggregate(orderbookIdentifier.toString()).publishes(new TradeExecutedEvent(orderbookIdentifier,
                                                                                               TOTAL_ITEMS,
                                                                                               99,
                                                                                               buyOrderIdentifier,
                                                                                               sellOrderIdentifier,
                                                                                               transactionIdentifier,
                                                                                               sellTransactionIdentifier))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(exactSequenceOf(ExecutedTransactionCommandMatcher
                                                                         .newInstance(TOTAL_ITEMS,
                                                                                      99,
                                                                                      transactionIdentifier),
                                                                 andNoMore()));
    }

    @Test
    public void testHandle_BuyTransactionExecuted() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString()).published(new BuyTransactionConfirmedEvent(
                transactionIdentifier))
               .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                                                                                                  TOTAL_ITEMS,
                                                                                                  99,
                                                                                                  buyOrderIdentifier,
                                                                                                  sellOrderIdentifier,
                                                                                                  transactionIdentifier,
                                                                                                  sellTransactionIdentifier))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionExecutedEvent(
                transactionIdentifier,
                TOTAL_ITEMS,
                99))
               .expectActiveSagas(0)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(
                               ConfirmMoneyReservationFromPortfolionCommandMatcher.newInstance(portfolioIdentifier,
                                                                                               TOTAL_ITEMS * 99),
                               AddItemsToPortfolioCommandMatcher.newInstance(portfolioIdentifier,
                                                                             orderbookIdentifier,
                                                                             TOTAL_ITEMS)));
    }

    @Test
    public void testHandle_BuyTransactionExecutedWithLowerExecutedPriceThanBidPrice() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(portfolioIdentifier,
                                                                                                 transactionIdentifier,
                                                                                                 TOTAL_ITEMS
                                                                                                         * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString())
               .published(new BuyTransactionConfirmedEvent(transactionIdentifier))
               .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                                                                                                  TOTAL_ITEMS,
                                                                                                  5,
                                                                                                  buyOrderIdentifier,
                                                                                                  sellOrderIdentifier,
                                                                                                  transactionIdentifier,
                                                                                                  sellTransactionIdentifier))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionExecutedEvent(
                transactionIdentifier,
                TOTAL_ITEMS,
                5))
               .expectActiveSagas(0)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(
                               CancelMoneyReservationFromPortfolioCommandMatcher.newInstance(portfolioIdentifier,
                                                                                             TOTAL_ITEMS * 5),
                               ConfirmMoneyReservationFromPortfolionCommandMatcher.newInstance(portfolioIdentifier,
                                                                                               TOTAL_ITEMS * 5),
                               AddItemsToPortfolioCommandMatcher.newInstance(portfolioIdentifier,
                                                                             orderbookIdentifier,
                                                                             TOTAL_ITEMS)));
    }

    @Test
    public void testHandle_BuyTransactionPartiallyExecuted() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString()).published(new BuyTransactionConfirmedEvent(
                transactionIdentifier))
               .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                                                                                                  50,
                                                                                                  99,
                                                                                                  buyOrderIdentifier,
                                                                                                  sellOrderIdentifier,
                                                                                                  transactionIdentifier,
                                                                                                  sellTransactionIdentifier))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionPartiallyExecutedEvent(
                transactionIdentifier,
                50,
                50,
                99))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(
                               ConfirmMoneyReservationFromPortfolionCommandMatcher
                                       .newInstance(portfolioIdentifier, 50 * 99),
                               AddItemsToPortfolioCommandMatcher
                                       .newInstance(portfolioIdentifier, orderbookIdentifier, 50)));
    }

    @Test
    public void testHandle_BuyTransactionPartiallyExecutedWithLowerExecutedPriceThanBidPrice() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(portfolioIdentifier,
                                                                                                 transactionIdentifier,
                                                                                                 TOTAL_ITEMS
                                                                                                         * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString())
               .published(new BuyTransactionConfirmedEvent(transactionIdentifier))
               .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                                                                                                  50,
                                                                                                  5,
                                                                                                  buyOrderIdentifier,
                                                                                                  sellOrderIdentifier,
                                                                                                  transactionIdentifier,
                                                                                                  sellTransactionIdentifier))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionPartiallyExecutedEvent(
                transactionIdentifier,
                50,
                50,
                5))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(
                               CancelMoneyReservationFromPortfolioCommandMatcher.newInstance(portfolioIdentifier, 250),
                               ConfirmMoneyReservationFromPortfolionCommandMatcher
                                       .newInstance(portfolioIdentifier, 50 * 5),
                               AddItemsToPortfolioCommandMatcher
                                       .newInstance(portfolioIdentifier, orderbookIdentifier, 50)));
    }

    @Test
    public void testHandle_MultipleBuyTransactionPartiallyExecuted() throws Exception {
        OrderId sellOrderIdentifier = new OrderId();
        OrderId buyOrderIdentifier = new OrderId();
        TransactionId sellTransactionIdentifier = new TransactionId();

        fixture.givenAggregate(transactionIdentifier.toString()).published(new BuyTransactionStartedEvent(
                transactionIdentifier,
                orderbookIdentifier,
                portfolioIdentifier,
                TOTAL_ITEMS,
                PRICE_PER_ITEM))
               .andThenAggregate(portfolioIdentifier.toString()).published(new CashReservedEvent(
                portfolioIdentifier, transactionIdentifier,
                TOTAL_ITEMS * PRICE_PER_ITEM))
               .andThenAggregate(transactionIdentifier.toString()).published(new BuyTransactionConfirmedEvent(
                transactionIdentifier))
               .andThenAggregate(orderbookIdentifier.toString()).published(new TradeExecutedEvent(orderbookIdentifier,
                                                                                                  50,
                                                                                                  99,
                                                                                                  buyOrderIdentifier,
                                                                                                  sellOrderIdentifier,
                                                                                                  transactionIdentifier,
                                                                                                  sellTransactionIdentifier))
               .whenAggregate(transactionIdentifier.toString()).publishes(new BuyTransactionPartiallyExecutedEvent(
                transactionIdentifier,
                50,
                50,
                99))
               .expectActiveSagas(1)
               .expectDispatchedCommandsMatching(
                       exactSequenceOf(
                               ConfirmMoneyReservationFromPortfolionCommandMatcher
                                       .newInstance(portfolioIdentifier, 50 * 99),
                               AddItemsToPortfolioCommandMatcher
                                       .newInstance(portfolioIdentifier, orderbookIdentifier, 50)));
    }
}
