/*
 * Copyright (c) 2011. Gridshore
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

package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jettro Coenradie
 */
public class Transaction extends AbstractAnnotatedAggregateRoot {
    private final static Logger logger = LoggerFactory.getLogger(Transaction.class);

    private long amountOfItems;
    private long amountOfExecutedItems;
    private TransactionType type;

    protected Transaction(AggregateIdentifier identifier) {
        super(identifier);
    }

    public Transaction(TransactionType type,
                       AggregateIdentifier orderbookIdentifier,
                       AggregateIdentifier portfolioIdentifier,
                       long amountOfItems,
                       long pricePerItem) {
        switch (type) {
            case BUY:
                apply(new BuyTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, amountOfItems, pricePerItem));
                break;
            case SELL:
                apply(new SellTransactionStartedEvent(orderbookIdentifier, portfolioIdentifier, amountOfItems, pricePerItem));
                break;

        }
    }

    public void confirm() {
        switch (this.type) {
            case BUY:
                apply(new BuyTransactionConfirmedEvent());
                break;
            case SELL:
                apply(new SellTransactionConfirmedEvent());
                break;
        }
    }

    public void cancel() {
        switch (this.type) {
            case BUY:
                apply(new BuyTransactionCancelledEvent(amountOfItems, amountOfExecutedItems));
                break;
            case SELL:
                apply(new SellTransactionCancelledEvent(amountOfItems, amountOfExecutedItems));
                break;
        }
    }

    public void execute(long amountOfItems, long itemPrice) {
        switch (this.type) {
            case BUY:
                if (isPartiallyExecuted(amountOfItems)) {
                    apply(new BuyTransactionPartiallyExecutedEvent(amountOfItems, amountOfItems + amountOfExecutedItems, itemPrice));
                } else {
                    apply(new BuyTransactionExecutedEvent(amountOfItems, itemPrice));
                }
                break;
            case SELL:
                if (isPartiallyExecuted(amountOfItems)) {
                    apply(new SellTransactionPartiallyExecutedEvent(amountOfItems, amountOfItems + amountOfExecutedItems, itemPrice));
                } else {
                    apply(new SellTransactionExecutedEvent(amountOfItems, itemPrice));
                }
                break;
        }
    }

    private boolean isPartiallyExecuted(long amountOfItems) {
        return this.amountOfExecutedItems + amountOfItems < this.amountOfItems;
    }

    @EventHandler
    public void onBuyTransactionStarted(BuyTransactionStartedEvent event) {
        this.amountOfItems = event.getTotalItems();
        this.type = TransactionType.BUY;
    }

    @EventHandler
    public void onSellTransactionStarted(SellTransactionStartedEvent event) {
        this.amountOfItems = event.getTotalItems();
        this.type = TransactionType.SELL;
    }

    @EventHandler
    public void onTransactionConfirmed(BuyTransactionConfirmedEvent event) {
        logger.debug("Buy transaction is confirmed, but we do not have to do anything. (Id of transaction is {}",
                getIdentifier().asString());
    }

    @EventHandler
    public void onTransactionConfirmed(SellTransactionConfirmedEvent event) {
        logger.debug("Sell transaction is confirmed, but we do not have to do anything. (Id of transaction is {}",
                getIdentifier().asString());
    }

    @EventHandler
    public void onTransactionCancelled(BuyTransactionCancelledEvent event) {
        // do nothing for now
    }

    @EventHandler
    public void onTransactionCancelled(SellTransactionCancelledEvent event) {
        // do nothing for now
    }

    @EventHandler
    public void onTransactionExecuted(BuyTransactionExecutedEvent event) {
        this.amountOfExecutedItems = this.amountOfItems;
    }

    @EventHandler
    public void onTransactionExecuted(SellTransactionExecutedEvent event) {
        this.amountOfExecutedItems = this.amountOfItems;
    }

    @EventHandler
    public void onTransactionPartiallyExecuted(SellTransactionPartiallyExecutedEvent event) {
        this.amountOfExecutedItems += event.getAmountOfExecutedItems();
    }

    @EventHandler
    public void onTransactionPartiallyExecuted(BuyTransactionPartiallyExecutedEvent event) {
        this.amountOfExecutedItems += event.getAmountOfExecutedItems();
    }
}
