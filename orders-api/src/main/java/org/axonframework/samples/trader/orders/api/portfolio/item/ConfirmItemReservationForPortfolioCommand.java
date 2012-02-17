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

package org.axonframework.samples.trader.orders.api.portfolio.item;

import org.axonframework.domain.AggregateIdentifier;

/**
 * Confirm the reserved items belonging to OrderBook of the provided identifier for the Portfolio of the provided
 * identifier.
 *
 * @author Jettro Coenradie
 */
public class ConfirmItemReservationForPortfolioCommand {

    private AggregateIdentifier portfolioIdentifier;
    private AggregateIdentifier orderBookIdentifier;
    private AggregateIdentifier transactionIdentifier;
    private long amountOfItemsToConfirm;

    public ConfirmItemReservationForPortfolioCommand(AggregateIdentifier portfolioIdentifier,
                                                     AggregateIdentifier orderBookIdentifier,
                                                     AggregateIdentifier transactionIdentifier,
                                                     long amountOfItemsToConfirm) {
        this.portfolioIdentifier = portfolioIdentifier;
        this.orderBookIdentifier = orderBookIdentifier;
        this.transactionIdentifier = transactionIdentifier;
        this.amountOfItemsToConfirm = amountOfItemsToConfirm;
    }

    public long getAmountOfItemsToConfirm() {
        return amountOfItemsToConfirm;
    }

    public AggregateIdentifier getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    public AggregateIdentifier getTransactionIdentifier() {
        return transactionIdentifier;
    }
}
