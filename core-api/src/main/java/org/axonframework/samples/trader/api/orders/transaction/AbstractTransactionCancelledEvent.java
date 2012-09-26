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

package org.axonframework.samples.trader.api.orders.transaction;

import org.axonframework.samples.trader.api.orders.trades.TransactionId;

/**
 * @author Jettro Coenradie
 */
public abstract class AbstractTransactionCancelledEvent {
    private TransactionId transactionIdentifier;
    private long totalAmountOfItems;
    private long amountOfExecutedItems;

    public AbstractTransactionCancelledEvent(TransactionId transactionIdentifier, long totalAmountOfItems, long amountOfExecutedItems) {
        this.transactionIdentifier = transactionIdentifier;
        this.totalAmountOfItems = totalAmountOfItems;
        this.amountOfExecutedItems = amountOfExecutedItems;
    }

    public TransactionId getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public long getAmountOfExecutedItems() {
        return amountOfExecutedItems;
    }

    public long getTotalAmountOfItems() {
        return totalAmountOfItems;
    }
}
