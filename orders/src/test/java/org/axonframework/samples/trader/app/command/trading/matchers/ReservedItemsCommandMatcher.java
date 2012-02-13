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

package org.axonframework.samples.trader.app.command.trading.matchers;

import org.axonframework.samples.trader.app.api.portfolio.item.ReserveItemsCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class ReservedItemsCommandMatcher extends BaseMatcher<ReserveItemsCommand> {
    private String orderbookIdentifier;
    private String portfolioIdentifier;
    private int amountOfReservedItems;

    public ReservedItemsCommandMatcher(String orderbookIdentifier, String portfolioIdentifier, int amountOfReservedItems) {
        this.orderbookIdentifier = orderbookIdentifier;
        this.portfolioIdentifier = portfolioIdentifier;
        this.amountOfReservedItems = amountOfReservedItems;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof ReserveItemsCommand)) {
            return false;
        }
        ReserveItemsCommand command = (ReserveItemsCommand) o;
        return command.getOrderBookIdentifier().asString().equals(orderbookIdentifier)
                && command.getPortfolioIdentifier().asString().equals(portfolioIdentifier)
                && amountOfReservedItems == command.getAmountOfItemsToReserve();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ReserveItemsCommand with amountOfReservedItems [")
                .appendValue(amountOfReservedItems)
                .appendText("] for OrderBook with identifier [")
                .appendValue(orderbookIdentifier)
                .appendText("] and for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("]");
    }
}
