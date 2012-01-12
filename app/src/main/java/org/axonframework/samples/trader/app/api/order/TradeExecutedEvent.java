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
public class TradeExecutedEvent extends DomainEvent {

    private final long tradeCount;
    private final long tradePrice;
    private final AggregateIdentifier buyOrderId;
    private final AggregateIdentifier sellOrderId;

    public TradeExecutedEvent(long tradeCount, long tradePrice, AggregateIdentifier buyOrderId, AggregateIdentifier sellOrderId) {
        this.tradeCount = tradeCount;
        this.tradePrice = tradePrice;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
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
}
