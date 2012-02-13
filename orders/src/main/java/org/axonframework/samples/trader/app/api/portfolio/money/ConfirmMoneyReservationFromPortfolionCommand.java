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

package org.axonframework.samples.trader.app.api.portfolio.money;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Jettro Coenradie
 */
public class ConfirmMoneyReservationFromPortfolionCommand {
    private AggregateIdentifier portfolioIdentifier;
    private AggregateIdentifier transactionIdentifier;
    private long amountOfMoneyToConfirmInCents;


    public ConfirmMoneyReservationFromPortfolionCommand(AggregateIdentifier portfolioIdentifier, AggregateIdentifier transactionIdentifier, long amountOfMoneyToConfirmInCents) {
        this.portfolioIdentifier = portfolioIdentifier;
        this.transactionIdentifier = transactionIdentifier;
        this.amountOfMoneyToConfirmInCents = amountOfMoneyToConfirmInCents;
    }

    public long getAmountOfMoneyToConfirmInCents() {
        return amountOfMoneyToConfirmInCents;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    public AggregateIdentifier getTransactionIdentifier() {
        return transactionIdentifier;
    }

    @Override
    public String toString() {
        return "ConfirmMoneyReservationFromPortfolionCommand{" +
                "amountOfMoneyToConfirmInCents=" + amountOfMoneyToConfirmInCents +
                ", portfolioIdentifier=" + portfolioIdentifier +
                ", transactionIdentifier=" + transactionIdentifier +
                '}';
    }
}
