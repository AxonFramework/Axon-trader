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

package org.axonframework.samples.trader.orders.command.matchers;

import org.axonframework.samples.trader.api.portfolio.stock.CancelItemReservationForPortfolioCommand;
import org.axonframework.samples.trader.api.orders.trades.OrderBookId;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class CancelItemReservationForPortfolioCommandMatcher
        extends BaseCommandMatcher<CancelItemReservationForPortfolioCommand> {

    private OrderBookId orderBookIdentifier;
    private PortfolioId portfolioIdentifier;
    private long amountOfItemsToCancel;

    public CancelItemReservationForPortfolioCommandMatcher(OrderBookId orderBookIdentifier,
                                                           PortfolioId portfolioIdentifier,
                                                           long amountOfItemsToCancel) {
        this.amountOfItemsToCancel = amountOfItemsToCancel;
        this.portfolioIdentifier = portfolioIdentifier;
        this.orderBookIdentifier = orderBookIdentifier;
    }

    @Override
    protected boolean doMatches(CancelItemReservationForPortfolioCommand command) {
        return command.getOrderBookIdentifier().equals(orderBookIdentifier)
                && command.getPortfolioIdentifier().equals(portfolioIdentifier)
                && command.getAmountOfItemsToCancel() == amountOfItemsToCancel;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("CancelItemReservationForPortfolioCommand with amountOfItemsToCancel [")
                .appendValue(amountOfItemsToCancel)
                .appendText("] for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("] and for OrderBook with identifier [")
                .appendValue(orderBookIdentifier)
                .appendText("]");
    }
}
