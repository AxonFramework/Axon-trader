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
import org.axonframework.samples.trader.app.api.portfolio.item.ItemsReservedEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.NotEnoughItemsAvailableToReserveInPortfolio;
import org.axonframework.samples.trader.app.api.transaction.SellTransactionStartedEvent;
import org.axonframework.samples.trader.app.command.trading.matchers.ConfirmItemReservationForPortfolioCommandMatcher;
import org.axonframework.samples.trader.app.command.trading.matchers.ReservedItemsCommandMatcher;
import org.axonframework.test.saga.AnnotatedSagaTestFixture;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
                .expectAssociationWith("orderbookIdentifier", orderbookIdentifier)
                .expectAssociationWith("portfolioIdentifier", portfolioIdentifier)
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(new ReservedItemsCommandMatcher(orderbookIdentifier.asString(), portfolioIdentifier.asString(), 100));
    }

    @Test
    public void testHandle_ItemsReserved() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .whenAggregate(portfolioIdentifier).publishes(new ItemsReservedEvent(orderbookIdentifier, 100))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier.asString(), portfolioIdentifier.asString(), 100));
    }

    @Test
    @Ignore
    public void testHandle_NotEnoughItemsToReserve() {
        fixture.givenAggregate(transactionIdentifier).published(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, 100, 10))
                .whenAggregate(portfolioIdentifier).publishes(new NotEnoughItemsAvailableToReserveInPortfolio(orderbookIdentifier, 50, 100))
                .expectActiveSagas(1)
                .expectDispatchedCommandsMatching(new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier.asString(), portfolioIdentifier.asString(), 100));
    }
}
