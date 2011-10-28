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

package org.axonframework.samples.trader.app.api.portfolio.reservation;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public class NotEnoughItemsAvailableToReserveInPortfolio extends DomainEvent {

    private AggregateIdentifier itemIdentifier;
    private int availableAmountOfItems;
    private int amountOfItemsToReserve;

    public NotEnoughItemsAvailableToReserveInPortfolio(AggregateIdentifier itemIdentifier, int availableAmountOfItems, int amountOfItemsToReserve) {
        this.itemIdentifier = itemIdentifier;
        this.availableAmountOfItems = availableAmountOfItems;
        this.amountOfItemsToReserve = amountOfItemsToReserve;
    }

    public int getAmountOfItemsToReserve() {
        return amountOfItemsToReserve;
    }

    public int getAvailableAmountOfItems() {
        return availableAmountOfItems;
    }

    public AggregateIdentifier getItemIdentifier() {
        return itemIdentifier;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return getAggregateIdentifier();
    }
}
