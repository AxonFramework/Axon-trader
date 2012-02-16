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

package org.axonframework.samples.trader.orders.command.matchers;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.orders.api.portfolio.item.AddItemsToPortfolioCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class AddItemsToPortfolioCommandMatcher extends BaseMatcher<AddItemsToPortfolioCommand> {
    private String orderBookIdentifier;
    private String portfolioIdentifier;
    private long amountOfItemsToAdd;

    public AddItemsToPortfolioCommandMatcher(AggregateIdentifier portfolioIdentifier, AggregateIdentifier orderBookIdentifier, long amountOfItemsToAdd) {
        this.amountOfItemsToAdd = amountOfItemsToAdd;
        this.portfolioIdentifier = portfolioIdentifier.asString();
        this.orderBookIdentifier = orderBookIdentifier.asString();
    }

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof AddItemsToPortfolioCommand)) {
            return false;
        }

        AddItemsToPortfolioCommand command = (AddItemsToPortfolioCommand) object;
        return command.getOrderBookIdentifier().asString().equals(orderBookIdentifier)
                && command.getPortfolioIdentifier().asString().equals(portfolioIdentifier)
                && command.getAmountOfItemsToAdd() == amountOfItemsToAdd;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("AddItemsToPortfolioCommand with amountOfItemsToAdd [")
                .appendValue(amountOfItemsToAdd)
                .appendText("] for OrderBook with identifier [")
                .appendValue(orderBookIdentifier)
                .appendText("] and for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("]");
    }
}
