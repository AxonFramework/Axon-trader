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

package org.axonframework.samples.trader.orders.api.transaction;

import org.axonframework.samples.trader.tradeengine.api.order.OrderBookId;
import org.axonframework.samples.trader.tradeengine.api.order.PortfolioId;

/**
 * @author Jettro Coenradie
 */
public abstract class AbstractStartTransactionCommand {

    private OrderBookId orderbookIdentifier;
    private PortfolioId portfolioIdentifier;
    private long tradeCount;
    private long itemPrice;

    public AbstractStartTransactionCommand(OrderBookId orderbookIdentifier,
                                           PortfolioId portfolioIdentifier, long tradeCount, long itemPrice) {
        this.itemPrice = itemPrice;
        this.orderbookIdentifier = orderbookIdentifier;
        this.portfolioIdentifier = portfolioIdentifier;
        this.tradeCount = tradeCount;
    }

    public long getItemPrice() {
        return itemPrice;
    }

    public OrderBookId getOrderbookIdentifier() {
        return orderbookIdentifier;
    }

    public PortfolioId getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    public long getTradeCount() {
        return tradeCount;
    }
}
