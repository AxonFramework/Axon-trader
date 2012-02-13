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

package org.axonframework.samples.trader.app.command.trading.matchers;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.orders.api.portfolio.money.DepositMoneyToPortfolioCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class DepositMoneyToPortfolioCommandMatcher extends BaseMatcher<DepositMoneyToPortfolioCommand> {
    private long moneyToAddInCents;
    private String portfolioIdentifier;

    public DepositMoneyToPortfolioCommandMatcher(AggregateIdentifier portfolioIdentifier, long moneyToAddInCents) {
        this.portfolioIdentifier = portfolioIdentifier.asString();
        this.moneyToAddInCents = moneyToAddInCents;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof DepositMoneyToPortfolioCommand)) {
            return false;
        }
        DepositMoneyToPortfolioCommand command = (DepositMoneyToPortfolioCommand) o;
        return moneyToAddInCents == command.getMoneyToAddInCents()
                && portfolioIdentifier.equals(command.getPortfolioIdentifier().asString());

    }

    @Override
    public void describeTo(Description description) {
        description.appendText("DepositMoneyToPortfolioCommand with moneyToAddInCents [")
                .appendValue(moneyToAddInCents)
                .appendText("] for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("]");

    }
}
