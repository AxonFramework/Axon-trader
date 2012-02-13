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
import org.axonframework.samples.trader.orders.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.orders.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.orders.api.portfolio.item.*;
import org.axonframework.samples.trader.orders.api.portfolio.money.*;
import org.axonframework.samples.trader.orders.command.trading.Portfolio;
import org.axonframework.samples.trader.orders.command.trading.PortfolioCommandHandler;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jettro Coenradie
 */
public class PortfolioCommandHandlerTest {
    private FixtureConfiguration fixture;
    private AggregateIdentifier portfolioIdentifier;
    private AggregateIdentifier orderBookIdentifier;
    private AggregateIdentifier transactionIdentifier;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        PortfolioCommandHandler commandHandler = new PortfolioCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(Portfolio.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
        portfolioIdentifier = fixture.getAggregateIdentifier();
        orderBookIdentifier = new UUIDAggregateIdentifier();
        transactionIdentifier = new UUIDAggregateIdentifier();
    }

    @Test
    public void testCreatePortfolio() {
        AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
        CreatePortfolioCommand command = new CreatePortfolioCommand(userIdentifier);
        fixture.given()
                .when(command)
                .expectEvents(new PortfolioCreatedEvent(userIdentifier));
    }

    /* Items related test methods */
    @Test
    public void testAddItemsToPortfolio() {
        AddItemsToPortfolioCommand command = new AddItemsToPortfolioCommand(portfolioIdentifier, orderBookIdentifier, 100);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()))
                .when(command)
                .expectEvents(new ItemsAddedToPortfolioEvent(orderBookIdentifier, 100));
    }

    @Test
    public void testReserveItems_noItemsAvailable() {
        ReserveItemsCommand command = new ReserveItemsCommand(portfolioIdentifier, orderBookIdentifier, transactionIdentifier, 200);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()))
                .when(command)
                .expectEvents(new ItemToReserveNotAvailableInPortfolioEvent(orderBookIdentifier, transactionIdentifier));
    }

    @Test
    public void testReserveItems_notEnoughItemsAvailable() {
        ReserveItemsCommand command = new ReserveItemsCommand(portfolioIdentifier, orderBookIdentifier, transactionIdentifier, 200);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()),
                new ItemsAddedToPortfolioEvent(orderBookIdentifier, 100))
                .when(command)
                .expectEvents(new NotEnoughItemsAvailableToReserveInPortfolio(orderBookIdentifier, transactionIdentifier, 100, 200));
    }

    @Test
    public void testReserveItems() {
        ReserveItemsCommand command = new ReserveItemsCommand(portfolioIdentifier, orderBookIdentifier, transactionIdentifier, 200);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()),
                new ItemsAddedToPortfolioEvent(orderBookIdentifier, 400))
                .when(command)
                .expectEvents(new ItemsReservedEvent(orderBookIdentifier, transactionIdentifier, 200));
    }

    @Test
    public void testConfirmationOfReservation() {
        ConfirmItemReservationForPortfolioCommand command =
                new ConfirmItemReservationForPortfolioCommand(portfolioIdentifier, orderBookIdentifier, transactionIdentifier, 100);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()),
                new ItemsAddedToPortfolioEvent(orderBookIdentifier, 400),
                new ItemsReservedEvent(orderBookIdentifier, transactionIdentifier, 100))
                .when(command)
                .expectEvents(new ItemReservationConfirmedForPortfolioEvent(orderBookIdentifier, transactionIdentifier, 100));
    }

    @Test
    public void testCancellationOfReservation() {
        CancelItemReservationForPortfolioCommand command =
                new CancelItemReservationForPortfolioCommand(portfolioIdentifier, orderBookIdentifier, transactionIdentifier, 100);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()),
                new ItemsAddedToPortfolioEvent(orderBookIdentifier, 400),
                new ItemsReservedEvent(orderBookIdentifier, transactionIdentifier, 100))
                .when(command)
                .expectEvents(new ItemReservationCancelledForPortfolioEvent(orderBookIdentifier, transactionIdentifier, 100));
    }

    /* Money related test methods */
    @Test
    public void testDepositingMoneyToThePortfolio() {
        DepositMoneyToPortfolioCommand command = new DepositMoneyToPortfolioCommand(portfolioIdentifier, 2000l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()))
                .when(command)
                .expectEvents(new MoneyDepositedToPortfolioEvent(2000l));

    }

    @Test
    public void testWithdrawingMoneyFromPortfolio() {
        WithdrawMoneyFromPortfolioCommand command = new WithdrawMoneyFromPortfolioCommand(portfolioIdentifier, 300l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(400))
                .when(command)
                .expectEvents(new MoneyWithdrawnFromPortfolioEvent(300l));
    }

    @Test
    public void testWithdrawingMoneyFromPortfolio_withoutEnoughMoney() {
        WithdrawMoneyFromPortfolioCommand command = new WithdrawMoneyFromPortfolioCommand(portfolioIdentifier, 300l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(200))
                .when(command)
                .expectEvents(new MoneyWithdrawnFromPortfolioEvent(300l));
    }

    @Test
    public void testMakingMoneyReservation() {
        ReserveMoneyFromPortfolioCommand command = new ReserveMoneyFromPortfolioCommand(portfolioIdentifier, transactionIdentifier, 300l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(400))
                .when(command)
                .expectEvents(new MoneyReservedFromPortfolioEvent(transactionIdentifier, 300l));
    }

    @Test
    public void testMakingMoneyReservation_withoutEnoughMoney() {
        ReserveMoneyFromPortfolioCommand command = new ReserveMoneyFromPortfolioCommand(portfolioIdentifier, transactionIdentifier, 600l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(400))
                .when(command)
                .expectEvents(new NotEnoughMoneyInPortfolioToMakeReservationEvent(transactionIdentifier, 600));
    }

    @Test
    public void testCancelMoneyReservation() {
        CancelMoneyReservationFromPortfolioCommand command = new CancelMoneyReservationFromPortfolioCommand(portfolioIdentifier, transactionIdentifier, 200l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(400))
                .when(command)
                .expectEvents(new MoneyReservationCancelledFromPortfolioEvent(transactionIdentifier, 200l));
    }

    @Test
    public void testConfirmMoneyReservation() {
        ConfirmMoneyReservationFromPortfolionCommand command = new ConfirmMoneyReservationFromPortfolionCommand(portfolioIdentifier, transactionIdentifier, 200l);
        fixture.given(new PortfolioCreatedEvent(new UUIDAggregateIdentifier()), new MoneyDepositedToPortfolioEvent(400))
                .when(command)
                .expectEvents(new MoneyReservationConfirmedFromPortfolioEvent(transactionIdentifier, 200l));
    }
}
