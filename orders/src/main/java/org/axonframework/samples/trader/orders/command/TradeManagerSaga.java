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

package org.axonframework.samples.trader.orders.command;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.samples.trader.api.orders.trades.OrderBookId;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.axonframework.samples.trader.api.orders.trades.TransactionId;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jettro Coenradie
 */
public abstract class TradeManagerSaga {

    private transient CommandBus commandBus;
    private long totalItems;
    private long pricePerItem;
    private TransactionId transactionIdentifier;
    private OrderBookId orderbookIdentifier;
    private PortfolioId portfolioIdentifier;

    /*-------------------------------------------------------------------------------------------*/
    /* Getters and setters                                                                       */
    /*-------------------------------------------------------------------------------------------*/
    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    protected CommandBus getCommandBus() {
        return commandBus;
    }

    protected OrderBookId getOrderbookIdentifier() {
        return orderbookIdentifier;
    }

    protected void setOrderbookIdentifier(OrderBookId orderbookIdentifier) {
        this.orderbookIdentifier = orderbookIdentifier;
    }

    protected PortfolioId getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    protected void setPortfolioIdentifier(PortfolioId portfolioIdentifier) {
        this.portfolioIdentifier = portfolioIdentifier;
    }

    protected long getPricePerItem() {
        return pricePerItem;
    }

    protected void setPricePerItem(long pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    protected long getTotalItems() {
        return totalItems;
    }

    protected void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    protected TransactionId getTransactionIdentifier() {
        return transactionIdentifier;
    }

    protected void setTransactionIdentifier(TransactionId transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }
}
