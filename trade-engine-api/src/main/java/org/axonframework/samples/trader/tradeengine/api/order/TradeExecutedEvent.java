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

package org.axonframework.samples.trader.tradeengine.api.order;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * <p>A new trade has been executed. The event contains the amount of items that are traded and the price for the items
 * that are traded. The event also contains the identifiers for the Buy Order and the Sell order.</p>
 *
 * @author Allard Buijze
 */
public class TradeExecutedEvent extends DomainEvent {

    private final long tradeCount;
    private final long tradePrice;
    private final AggregateIdentifier buyOrderId;
    private final AggregateIdentifier sellOrderId;
    private final AggregateIdentifier buyTransactionId;
    private final AggregateIdentifier sellTransactionId;

    public TradeExecutedEvent(long tradeCount,
                              long tradePrice,
                              AggregateIdentifier buyOrderId,
                              AggregateIdentifier sellOrderId,
                              AggregateIdentifier buyTransactionId,
                              AggregateIdentifier sellTransactionId) {
        this.tradeCount = tradeCount;
        this.tradePrice = tradePrice;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.sellTransactionId = sellTransactionId;
        this.buyTransactionId = buyTransactionId;
    }

    public AggregateIdentifier getOrderBookIdentifier() {
        return getAggregateIdentifier();
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public long getTradePrice() {
        return tradePrice;
    }

    public AggregateIdentifier getBuyOrderId() {
        return buyOrderId;
    }

    public AggregateIdentifier getSellOrderId() {
        return sellOrderId;
    }

    public AggregateIdentifier getBuyTransactionId() {
        return buyTransactionId;
    }

    public AggregateIdentifier getSellTransactionId() {
        return sellTransactionId;
    }
}
