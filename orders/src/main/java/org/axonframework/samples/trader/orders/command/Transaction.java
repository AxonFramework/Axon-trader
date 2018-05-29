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

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.samples.trader.api.orders.TransactionType;
import org.axonframework.samples.trader.api.orders.transaction.*;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate(repository = "transactionAggregateRepository")
public class Transaction {

    @AggregateIdentifier
    private TransactionId transactionId;
    private long amountOfItems;
    private long amountOfExecutedItems;
    private TransactionType type;

    @SuppressWarnings("UnusedDeclaration")
    public Transaction() {
        // Required by Axon Framework
    }

    @CommandHandler
    public Transaction(StartBuyTransactionCommand cmd) {
        apply(new BuyTransactionStartedEvent(cmd.getTransactionId(),
                                             cmd.getOrderBookId(),
                                             cmd.getPortfolioId(),
                                             cmd.getTradeCount(),
                                             cmd.getPricePerItem()));
    }

    @CommandHandler
    public Transaction(StartSellTransactionCommand cmd) {
        apply(new SellTransactionStartedEvent(cmd.getTransactionId(),
                                              cmd.getOrderBookId(),
                                              cmd.getPortfolioId(),
                                              cmd.getTradeCount(),
                                              cmd.getPricePerItem()));
    }

    @SuppressWarnings("unused")
    @CommandHandler
    public void handle(ConfirmTransactionCommand cmd) {
        if (type == TransactionType.BUY) {
            apply(new BuyTransactionConfirmedEvent(transactionId));
        } else if (type == TransactionType.SELL) {
            apply(new SellTransactionConfirmedEvent(transactionId));
        }
    }

    @SuppressWarnings("unused")
    @CommandHandler
    public void handle(CancelTransactionCommand cmd) {
        if (type == TransactionType.BUY) {
            apply(new BuyTransactionCancelledEvent(transactionId, amountOfItems, amountOfExecutedItems));
        } else if (type == TransactionType.SELL) {
            apply(new SellTransactionCancelledEvent(transactionId, amountOfItems, amountOfExecutedItems));
        }
    }

    @CommandHandler
    public void handle(ExecutedTransactionCommand cmd) {
        long amountOfItems = cmd.getAmountOfItems();
        long itemPrice = cmd.getItemPrice();
        if (type == TransactionType.BUY) {
            if (isPartiallyExecuted(amountOfItems)) {
                apply(new BuyTransactionPartiallyExecutedEvent(transactionId,
                                                               amountOfItems,
                                                               amountOfItems + amountOfExecutedItems,
                                                               itemPrice));
            } else {
                apply(new BuyTransactionExecutedEvent(transactionId, amountOfItems, itemPrice));
            }
        } else if (type == TransactionType.SELL) {
            if (isPartiallyExecuted(amountOfItems)) {
                apply(new SellTransactionPartiallyExecutedEvent(transactionId,
                                                                amountOfItems,
                                                                amountOfItems + amountOfExecutedItems,
                                                                itemPrice));
            } else {
                apply(new SellTransactionExecutedEvent(transactionId, amountOfItems, itemPrice));
            }
        }
    }

    private boolean isPartiallyExecuted(long amountOfItems) {
        return amountOfExecutedItems + amountOfItems < this.amountOfItems;
    }

    @EventSourcingHandler
    public void on(BuyTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        amountOfItems = event.getTotalItems();
        type = TransactionType.BUY;
    }

    @EventSourcingHandler
    public void on(SellTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        amountOfItems = event.getTotalItems();
        type = TransactionType.SELL;
    }

    @SuppressWarnings("unused")
    @EventSourcingHandler
    public void on(BuyTransactionExecutedEvent event) {
        amountOfExecutedItems = amountOfItems;
    }

    @SuppressWarnings("unused")
    @EventSourcingHandler
    public void on(SellTransactionExecutedEvent event) {
        amountOfExecutedItems = amountOfItems;
    }

    @EventSourcingHandler
    public void on(SellTransactionPartiallyExecutedEvent event) {
        amountOfExecutedItems += event.getAmountOfExecutedItems();
    }

    @EventSourcingHandler
    public void on(BuyTransactionPartiallyExecutedEvent event) {
        amountOfExecutedItems += event.getAmountOfExecutedItems();
    }
}
