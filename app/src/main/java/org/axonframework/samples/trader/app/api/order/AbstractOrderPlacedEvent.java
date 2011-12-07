/*
 * Copyright (c) 2010. Gridshore
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

package org.axonframework.samples.trader.app.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Allard Buijze
 */
public abstract class AbstractOrderPlacedEvent extends DomainEvent {

    private final AggregateIdentifier orderId;
    private final long tradeCount;
    private final int itemPrice;
    private final AggregateIdentifier portfolioId;

    protected AbstractOrderPlacedEvent(AggregateIdentifier orderId, long tradeCount, int itemPrice, AggregateIdentifier portfolioId) {
        this.orderId = orderId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.portfolioId = portfolioId;
    }

    public AggregateIdentifier orderBookIdentifier() {
        return getAggregateIdentifier();
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public AggregateIdentifier getPortfolioId() {
        return portfolioId;
    }

}
