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

import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.stock.ConfirmItemReservationForPortfolioCommand;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ConfirmItemReservationForPortfolioCommandMatcher
        extends BaseCommandMatcher<ConfirmItemReservationForPortfolioCommand> {

    private OrderBookId orderbookIdentifier;
    private PortfolioId portfolioIdentifier;
    private int amountOfConfirmedItems;

    public ConfirmItemReservationForPortfolioCommandMatcher(
            OrderBookId orderbookIdentifier, PortfolioId portfolioIdentifier, int amountOfConfirmedItems) {
        this.orderbookIdentifier = orderbookIdentifier;
        this.portfolioIdentifier = portfolioIdentifier;
        this.amountOfConfirmedItems = amountOfConfirmedItems;
    }

    public static Matcher newInstance(OrderBookId orderbookIdentifier, PortfolioId portfolioIdentifier,
                                      int amountOfConfirmedItems) {
        return new ConfirmItemReservationForPortfolioCommandMatcher(orderbookIdentifier,
                                                                    portfolioIdentifier,
                                                                    amountOfConfirmedItems);
    }

    @Override
    protected boolean doMatches(ConfirmItemReservationForPortfolioCommand command) {
        return command.getOrderBookId().equals(orderbookIdentifier)
                && command.getPortfolioId().equals(portfolioIdentifier)
                && amountOfConfirmedItems == command.getAmountOfItemsToConfirm();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ConfirmItemReservationForPortfolioCommand with amountOfConfirmedItems [")
                   .appendValue(amountOfConfirmedItems)
                   .appendText("] for OrderBook with identifier [")
                   .appendValue(orderbookIdentifier)
                   .appendText("] and for Portfolio with identifier [")
                   .appendValue(portfolioIdentifier)
                   .appendText("]");
    }
}
