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

import java.io.Serializable;

/**
 * <p>A new trade has been executed. The event contains the amount of items that are traded and the price for the items
 * that are traded. The event also contains the identifiers for the Buy Order and the Sell order.</p>
 *
 * @author Allard Buijze
 */
public class TradeExecutedEvent implements Serializable {
    private static final long serialVersionUID = 6292249351659536792L;

    private final long tradeCount;
    private final long tradePrice;
    private final OrderId buyOrderId;
    private final OrderId sellOrderId;
    private final TransactionId buyTransactionId;
    private final TransactionId sellTransactionId;
    private final OrderBookId orderBookId;

    public TradeExecutedEvent(OrderBookId orderBookId,
                              long tradeCount,
                              long tradePrice,
                              OrderId buyOrderId,
                              OrderId sellOrderId,
                              TransactionId buyTransactionId,
                              TransactionId sellTransactionId) {
        this.tradeCount = tradeCount;
        this.tradePrice = tradePrice;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.sellTransactionId = sellTransactionId;
        this.buyTransactionId = buyTransactionId;
        this.orderBookId = orderBookId;
    }

    public OrderBookId getOrderBookIdentifier() {
        return this.orderBookId;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public long getTradePrice() {
        return tradePrice;
    }

    public OrderId getBuyOrderId() {
        return buyOrderId;
    }

    public OrderId getSellOrderId() {
        return sellOrderId;
    }

    public TransactionId getBuyTransactionId() {
        return buyTransactionId;
    }

    public TransactionId getSellTransactionId() {
        return sellTransactionId;
    }
}
