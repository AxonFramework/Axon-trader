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

package org.axonframework.samples.trader.app.api.transaction;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public abstract class AbstractTransactionStartedEvent extends DomainEvent {
    private AggregateIdentifier orderbookIdentifier;
    private AggregateIdentifier portfolioIdentifier;
    private long totalItems;
    private long pricePerItem;

    public AbstractTransactionStartedEvent(AggregateIdentifier orderbookIdentifier,
                                           AggregateIdentifier portfolioIdentifier,
                                           long totalItems,
                                           long pricePerItem) {
        this.orderbookIdentifier = orderbookIdentifier;
        this.portfolioIdentifier = portfolioIdentifier;
        this.totalItems = totalItems;
        this.pricePerItem = pricePerItem;
    }

    public AggregateIdentifier getOrderbookIdentifier() {
        return orderbookIdentifier;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    public AggregateIdentifier getTransactionIdentifier() {
        return this.getAggregateIdentifier();
    }

    public long getPricePerItem() {
        return pricePerItem;
    }

    public long getTotalItems() {
        return totalItems;
    }

}
