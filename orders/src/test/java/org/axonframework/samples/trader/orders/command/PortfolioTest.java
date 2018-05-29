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
import org.axonframework.samples.trader.api.orders.transaction.TransactionId;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.cash.*;
import org.axonframework.samples.trader.api.portfolio.stock.*;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class PortfolioTest {

    private AggregateTestFixture<Portfolio> fixture;

    private PortfolioId portfolioId = new PortfolioId();
    private OrderBookId orderBookId = new OrderBookId();
    private TransactionId transactionId = new TransactionId();
    private UserId userId = new UserId();

    private PortfolioCreatedEvent portfolioCreatedEvent;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Portfolio.class);

        portfolioCreatedEvent = new PortfolioCreatedEvent(portfolioId, userId);
    }

    @Test
    public void testCreatePortfolio() {
        fixture.givenNoPriorActivity()
               .when(new CreatePortfolioCommand(portfolioId, userId))
               .expectEvents(portfolioCreatedEvent);
    }

    @Test
    public void testAddItemsToPortfolio() {
        fixture.given(portfolioCreatedEvent)
               .when(new AddItemsToPortfolioCommand(portfolioId, orderBookId, 100L))
               .expectEvents(new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 100L));
    }

    @Test
    public void testReserveItems_noItemsAvailable() {
        fixture.given(portfolioCreatedEvent)
               .when(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, 200L))
               .expectEvents(new ItemToReserveNotAvailableInPortfolioEvent(portfolioId, orderBookId, transactionId));
    }

    @Test
    public void testReserveItems_notEnoughItemsAvailable() {
        NotEnoughItemsAvailableToReserveInPortfolioEvent expectedEvent =
                new NotEnoughItemsAvailableToReserveInPortfolioEvent(portfolioId,
                                                                     orderBookId,
                                                                     transactionId,
                                                                     100L,
                                                                     200L);

        fixture.given(portfolioCreatedEvent, new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 100L))
               .when(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, 200L))
               .expectEvents(expectedEvent);
    }

    @Test
    public void testReserveItems_notEnoughItemsAvailableAfterReservation() {
        ItemReservationConfirmedForPortfolioEvent testItemReservationConfirmedForPortfolioEvent =
                new ItemReservationConfirmedForPortfolioEvent(portfolioId, orderBookId, transactionId, 50L);

        NotEnoughItemsAvailableToReserveInPortfolioEvent expectedEvent =
                new NotEnoughItemsAvailableToReserveInPortfolioEvent(portfolioId,
                                                                     orderBookId,
                                                                     transactionId,
                                                                     50L,
                                                                     100L);

        fixture.given(portfolioCreatedEvent, new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 100L),
                      new ItemsReservedEvent(portfolioId, orderBookId, transactionId, 50L),
                      testItemReservationConfirmedForPortfolioEvent)
               .when(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, 100L))
               .expectEvents(expectedEvent);
    }

    @Test
    public void testReserveItems_notEnoughItemsAvailableAfterReservationConfirmation() {
        NotEnoughItemsAvailableToReserveInPortfolioEvent expectedEvent =
                new NotEnoughItemsAvailableToReserveInPortfolioEvent(portfolioId, orderBookId, transactionId, 50L, 75L);

        fixture.given(portfolioCreatedEvent,
                      new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 100L),
                      new ItemsReservedEvent(portfolioId, orderBookId, transactionId, 50L),
                      new ItemReservationConfirmedForPortfolioEvent(portfolioId, orderBookId, transactionId, 50L))
               .when(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, 75L))
               .expectEvents(expectedEvent);
    }

    @Test
    public void testReserveItems() {
        fixture.given(portfolioCreatedEvent, new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 400L))
               .when(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, 200L))
               .expectEvents(new ItemsReservedEvent(portfolioId, orderBookId, transactionId, 200L));
    }

    @Test
    public void testConfirmationOfReservation() {
        ItemReservationConfirmedForPortfolioEvent expectedEvent
                = new ItemReservationConfirmedForPortfolioEvent(portfolioId, orderBookId, transactionId, 100L);

        fixture.given(portfolioCreatedEvent,
                      new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 400L),
                      new ItemsReservedEvent(portfolioId, orderBookId, transactionId, 100L))
               .when(new ConfirmItemReservationForPortfolioCommand(portfolioId, orderBookId, transactionId, 100L))
               .expectEvents(expectedEvent);
    }

    @Test
    public void testCancellationOfReservation() {
        ItemReservationCancelledForPortfolioEvent expectedEvent =
                new ItemReservationCancelledForPortfolioEvent(portfolioId, orderBookId, transactionId, 100L);

        fixture.given(portfolioCreatedEvent,
                      new ItemsAddedToPortfolioEvent(portfolioId, orderBookId, 400L),
                      new ItemsReservedEvent(portfolioId, orderBookId, transactionId, 100L))
               .when(new CancelItemReservationForPortfolioCommand(portfolioId, orderBookId, transactionId, 100L))
               .expectEvents(expectedEvent);
    }


    @Test
    public void testDepositingMoneyToThePortfolio() {
        fixture.given(portfolioCreatedEvent)
               .when(new DepositCashCommand(portfolioId, 2000L))
               .expectEvents(new CashDepositedEvent(portfolioId, 2000L));
    }

    @Test
    public void testWithdrawingMoneyFromPortfolio() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 400L))
               .when(new WithdrawCashCommand(portfolioId, 300L))
               .expectEvents(new CashWithdrawnEvent(portfolioId, 300L));
    }

    @Test
    public void testWithdrawingMoneyFromPortfolio_withoutEnoughMoney() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 200L))
               .when(new WithdrawCashCommand(portfolioId, 300L))
               .expectEvents(new CashWithdrawnEvent(portfolioId, 300L));
    }

    @Test
    public void testMakingMoneyReservation() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 400L))
               .when(new ReserveCashCommand(portfolioId, transactionId, 300L))
               .expectEvents(new CashReservedEvent(portfolioId, transactionId, 300L));
    }

    @Test
    public void testMakingMoneyReservation_withoutEnoughMoney() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 400L))
               .when(new ReserveCashCommand(portfolioId, transactionId, 600L))
               .expectEvents(new CashReservationRejectedEvent(portfolioId, transactionId, 600L));
    }

    @Test
    public void testCancelMoneyReservation() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 400L))
               .when(new CancelCashReservationCommand(portfolioId, transactionId, 200L))
               .expectEvents(new CashReservationCancelledEvent(portfolioId, transactionId, 200L));
    }

    @Test
    public void testConfirmMoneyReservation() {
        fixture.given(portfolioCreatedEvent, new CashDepositedEvent(portfolioId, 400L))
               .when(new ConfirmCashReservationCommand(portfolioId, transactionId, 200L))
               .expectEvents(new CashReservationConfirmedEvent(portfolioId, transactionId, 200L));
    }
}
