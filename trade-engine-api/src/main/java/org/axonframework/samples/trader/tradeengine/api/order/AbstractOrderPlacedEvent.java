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

package org.axonframework.samples.trader.tradeengine.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * <p>Abstract parent class for all buy and sell order placed events.</p>
 *
 * @author Allard Buijze
 */
public abstract class AbstractOrderPlacedEvent extends DomainEvent {

    private final AggregateIdentifier orderId;
    private AggregateIdentifier transactionId;
    private final long tradeCount;
    private final long itemPrice;
    private final AggregateIdentifier portfolioId;

    protected AbstractOrderPlacedEvent(AggregateIdentifier orderId, AggregateIdentifier transactionId, long tradeCount,
                                       long itemPrice, AggregateIdentifier portfolioId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
        this.portfolioId = portfolioId;
    }

    public AggregateIdentifier orderBookIdentifier() {
        return getAggregateIdentifier();
    }

    public AggregateIdentifier getTransactionIdentifier() {
        return transactionId;
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public long getItemPrice() {
        return itemPrice;
    }

    public AggregateIdentifier getPortfolioId() {
        return portfolioId;
    }
}
