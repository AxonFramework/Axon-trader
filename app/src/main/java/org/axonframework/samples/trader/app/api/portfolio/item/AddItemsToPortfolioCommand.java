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

package org.axonframework.samples.trader.app.api.portfolio.item;

import org.axonframework.domain.AggregateIdentifier;

/**
 * Try to add new items for a specific OrderBook to the portfolio.
 *
 * @author Jettro Coenradie
 */
public class AddItemsToPortfolioCommand {
    private AggregateIdentifier portfolioIdentifier;
    private AggregateIdentifier orderBookIdentifier;
    private long amountOfItemsToAdd;

    /**
     * Create a new command.
     *
     * @param portfolioIdentifier Identifier of the Portfolio to add items to
     * @param orderBookIdentifier Identifier of the OrderBook to add items for
     * @param amountOfItemsToAdd  AMount of items to add
     */
    public AddItemsToPortfolioCommand(AggregateIdentifier portfolioIdentifier, AggregateIdentifier orderBookIdentifier, long amountOfItemsToAdd) {
        this.portfolioIdentifier = portfolioIdentifier;
        this.orderBookIdentifier = orderBookIdentifier;
        this.amountOfItemsToAdd = amountOfItemsToAdd;
    }

    public long getAmountOfItemsToAdd() {
        return amountOfItemsToAdd;
    }

    public AggregateIdentifier getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return portfolioIdentifier;
    }
}
