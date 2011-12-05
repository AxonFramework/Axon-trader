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

import org.axonframework.samples.trader.app.api.portfolio.item.ConfirmItemReservationForPortfolioCommand;

/**
 * @author Jettro Coenradie
 */
public class ConfirmItemReservationForPortfolioCommandMatcher extends TradeManagerSagaMatcher<ConfirmItemReservationForPortfolioCommand> {
    private String itemIdentifier;
    private String portfolioIdentifier;
    private int amountOfConfirmedItems;

    public ConfirmItemReservationForPortfolioCommandMatcher(String itemIdentifier, String portfolioIdentifier, int amountOfConfirmedItems) {
        this.itemIdentifier = itemIdentifier;
        this.portfolioIdentifier = portfolioIdentifier;
        this.amountOfConfirmedItems = amountOfConfirmedItems;
    }

    @Override
    public boolean doMatch(ConfirmItemReservationForPortfolioCommand command) {
        if (!command.getOrderBookIdentifier().asString().equals(itemIdentifier)) {
            problem = String.format("Item identifier is not as expected, required %s but received %s", itemIdentifier, command.getOrderBookIdentifier());
            return false;
        }
        if (!command.getPortfolioIdentifier().asString().equals(portfolioIdentifier)) {
            problem = String.format("Portfolio identifier is not as expected, required %s but received %s", portfolioIdentifier, command.getPortfolioIdentifier());
            return false;
        }
        if (amountOfConfirmedItems != command.getAmountOfItemsToConfirm()) {
            problem = String.format("Wrong amount of confirmed items, required %d but received %d", amountOfConfirmedItems, command.getAmountOfItemsToConfirm());
            return false;
        }
        return true;
    }
}
