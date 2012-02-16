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

package org.axonframework.samples.trader.tradeengine.command;

import org.axonframework.domain.AggregateIdentifier;

/**
 * @author Allard Buijze
 */
class Order {

    private AggregateIdentifier transactionId;
    private final long itemPrice;
    private final long tradeCount;
    private final AggregateIdentifier userId;
    private long itemsRemaining;
    private AggregateIdentifier orderId;

    public Order(AggregateIdentifier orderId, AggregateIdentifier transactionId, long itemPrice, long tradeCount, AggregateIdentifier userId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.itemsRemaining = tradeCount;
        this.userId = userId;
    }

    public long getItemPrice() {
        return itemPrice;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public AggregateIdentifier getUserId() {
        return userId;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    public AggregateIdentifier getOrderId() {
        return orderId;
    }

    public void recordTraded(long tradeCount) {
        this.itemsRemaining -= tradeCount;
    }

    public AggregateIdentifier getTransactionId() {
        return transactionId;
    }
}
