/*
 * Copyright (c) 2012. Gridshore
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
import org.axonframework.samples.trader.app.api.portfolio.money.MoneyReservedFromPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.money.NotEnoughMoneyInPortfolioToMakeReservationEvent;
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
public class BuyTradeManagerSagaTest {
    private static final long TOTAL_ITEMS = 100;
    private static final long PRICE_PER_ITEM = 10;

    private AggregateIdentifier transactionIdentifier = new UUIDAggregateIdentifier();
    private AggregateIdentifier orderbookIdentifier = new UUIDAggregateIdentifier();
    private AggregateIdentifier portfolioIdentifier = new UUIDAggregateIdentifier();

    private AnnotatedSagaTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AnnotatedSagaTestFixture(BuyTradeManagerSaga.class);
    }

    @Test
    public void testHandle_SellTransactionStarted() throws Exception {
        fixture.givenAggregate(transactionIdentifier).published()
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ReserveMoneyFromPortfolioCommandMatcher(portfolioIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_MoneyIsReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .whenAggregate(portfolioIdentifier).publishes(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ConfirmTransactionCommandMatcher(transactionIdentifier)));
    }

    @Test
    public void testHandle_NotEnoughMoneyToReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .whenAggregate(portfolioIdentifier).publishes(new NotEnoughMoneyInPortfolioToMakeReservationEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .expectActiveSagas(0);
    }

    @Test
    public void testHandle_TransactionConfirmed() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionConfirmedEvent())
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CreateBuyOrderCommandMatcher(portfolioIdentifier, orderbookIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TransactionCancelled() {
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionCancelledEvent(TOTAL_ITEMS, 0))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new CancelMoneyReservationFromPortfolioCommandMatcher(portfolioIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM)));
    }

    @Test
    public void testHandle_TradeExecutedPlaced() {
        AggregateIdentifier sellOrderIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier sellTransactionIdentifier = new UUIDAggregateIdentifier();
        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent())
                .whenAggregate(orderbookIdentifier).publishes(new TradeExecutedEvent(TOTAL_ITEMS, 99, transactionIdentifier, sellOrderIdentifier, transactionIdentifier, sellTransactionIdentifier))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(exactSequenceOf(new ExecutedTransactionCommandMatcher(TOTAL_ITEMS, 99, transactionIdentifier), andNoMore()));

    }

    @Test
    public void testHandle_BuyTransactionExecuted() {
        AggregateIdentifier sellOrderIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier sellTransactionIdentifier = new UUIDAggregateIdentifier();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent())
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(TOTAL_ITEMS, 99, transactionIdentifier, sellOrderIdentifier, transactionIdentifier, sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionExecutedEvent(TOTAL_ITEMS, 99))
                .expectActiveSagas(0)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier, TOTAL_ITEMS * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier, orderbookIdentifier, TOTAL_ITEMS)));
    }

    @Test
    public void testHandle_BuyTransactionPartiallyExecuted() {
        AggregateIdentifier sellOrderIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier sellTransactionIdentifier = new UUIDAggregateIdentifier();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent())
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(50, 99, orderbookIdentifier, sellOrderIdentifier, transactionIdentifier, sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionPartiallyExecutedEvent(50, 50, 99))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier, 50 * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier, orderbookIdentifier, 50)));
    }

    @Test
    public void testHandle_MultipleBuyTransactionPartiallyExecuted() {
        AggregateIdentifier sellOrderIdentifier = new UUIDAggregateIdentifier();
        AggregateIdentifier sellTransactionIdentifier = new UUIDAggregateIdentifier();

        fixture.givenAggregate(transactionIdentifier).published(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, TOTAL_ITEMS, PRICE_PER_ITEM))
                .andThenAggregate(portfolioIdentifier).published(new MoneyReservedFromPortfolioEvent(transactionIdentifier, TOTAL_ITEMS * PRICE_PER_ITEM))
                .andThenAggregate(transactionIdentifier).published(new BuyTransactionConfirmedEvent())
                .andThenAggregate(orderbookIdentifier).published(new TradeExecutedEvent(50, 99, orderbookIdentifier, sellOrderIdentifier, transactionIdentifier, sellTransactionIdentifier))
                .whenAggregate(transactionIdentifier).publishes(new BuyTransactionPartiallyExecutedEvent(50, 50, 99))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(
                        exactSequenceOf(
                                new ConfirmMoneyReservationFromPortfolionCommandMatcher(portfolioIdentifier, 50 * 99),
                                new AddItemsToPortfolioCommandMatcher(portfolioIdentifier, orderbookIdentifier, 50)));
    }

}
