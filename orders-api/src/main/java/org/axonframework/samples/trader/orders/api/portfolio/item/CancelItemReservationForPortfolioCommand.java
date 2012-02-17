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
 * Cancel a reservation for an amount of items for the OrderBook belonging to the provided identifier in the Portfolio
 * of the provided identifier.
 *
 * @author Jettro Coenradie
 */
public class CancelItemReservationForPortfolioCommand {

    private AggregateIdentifier portfolioIdentifier;
    private AggregateIdentifier orderBookIdentifier;
    private AggregateIdentifier transactionIdentifier;
    private long amountOfCancelledItems;

    public CancelItemReservationForPortfolioCommand(AggregateIdentifier portfolioIdentifier,
                                                    AggregateIdentifier orderBookIdentifier,
                                                    AggregateIdentifier transactionIdentifier,
                                                    long amountOfCancelledItems) {
        this.portfolioIdentifier = portfolioIdentifier;
        this.orderBookIdentifier = orderBookIdentifier;
        this.transactionIdentifier = transactionIdentifier;

        this.amountOfCancelledItems = amountOfCancelledItems;
    }

    public long getAmountOfItemsToCancel() {
        return amountOfCancelledItems;
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
