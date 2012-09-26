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

import org.axonframework.samples.trader.api.portfolio.money.ConfirmMoneyReservationFromPortfolionCommand;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class ConfirmMoneyReservationFromPortfolionCommandMatcher
        extends BaseCommandMatcher<ConfirmMoneyReservationFromPortfolionCommand> {

    private PortfolioId portfolioIdentifier;
    private long amountOfMoneyToconfirm;

    public ConfirmMoneyReservationFromPortfolionCommandMatcher(PortfolioId portfolioIdentifier,
                                                               long amountOfMoneyToconfirm) {
        this.portfolioIdentifier = portfolioIdentifier;
        this.amountOfMoneyToconfirm = amountOfMoneyToconfirm;
    }

    @Override
    protected boolean doMatches(ConfirmMoneyReservationFromPortfolionCommand command) {
        return command.getPortfolioIdentifier().equals(portfolioIdentifier)
                && command.getAmountOfMoneyToConfirmInCents() == amountOfMoneyToconfirm;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ConfirmMoneyReservationFromPortfolionCommand with amountOfMoneyToConfirm [")
                .appendValue(amountOfMoneyToconfirm)
                .appendText("] for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("]");
    }
}
