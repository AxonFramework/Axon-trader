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

package org.axonframework.samples.trader.query.transaction;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.orders.TransactionType;
import org.axonframework.samples.trader.api.orders.transaction.AbstractTransactionExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.AbstractTransactionPartiallyExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.AbstractTransactionStartedEvent;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionCancelledEvent;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionConfirmedEvent;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionPartiallyExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionStartedEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionCancelledEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionConfirmedEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionPartiallyExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionStartedEvent;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.transaction.repositories.TransactionViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("queryModel")
public class TransactionEventHandler {

    private final OrderBookViewRepository orderBookViewRepository;
    private final TransactionViewRepository transactionViewRepository;

    @Autowired
    public TransactionEventHandler(OrderBookViewRepository orderBookViewRepository,
                                   TransactionViewRepository transactionViewRepository) {
        this.orderBookViewRepository = orderBookViewRepository;
        this.transactionViewRepository = transactionViewRepository;
    }

    @EventHandler
    public void on(BuyTransactionStartedEvent event) {
        startTransaction(event, TransactionType.BUY);
    }

    @EventHandler
    public void on(SellTransactionStartedEvent event) {
        startTransaction(event, TransactionType.SELL);
    }

    private void startTransaction(AbstractTransactionStartedEvent event, TransactionType type) {
        OrderBookView orderBookView = orderBookViewRepository.findOne(event.getOrderBookId().toString());

        TransactionView entry = new TransactionView();
        entry.setAmountOfExecutedItems(0);
        entry.setAmountOfItems((int) event.getTotalItems());
        entry.setPricePerItem(event.getPricePerItem());
        entry.setIdentifier(event.getTransactionId().toString());
        entry.setOrderBookId(event.getOrderBookId().toString());
        entry.setPortfolioId(event.getPortfolioId().toString());
        entry.setState(TransactionState.STARTED);
        entry.setType(type);
        entry.setCompanyName(orderBookView.getCompanyName());

        transactionViewRepository.save(entry);
    }

    @EventHandler
    public void on(BuyTransactionCancelledEvent event) {
        changeStateOfTransaction(event.getTransactionId().toString(), TransactionState.CANCELLED);
    }

    @EventHandler
    public void on(SellTransactionCancelledEvent event) {
        changeStateOfTransaction(event.getTransactionId().toString(), TransactionState.CANCELLED);
    }

    @EventHandler
    public void on(BuyTransactionConfirmedEvent event) {
        changeStateOfTransaction(event.getTransactionId().toString(), TransactionState.CONFIRMED);
    }

    @EventHandler
    public void on(SellTransactionConfirmedEvent event) {
        changeStateOfTransaction(event.getTransactionId().toString(), TransactionState.CONFIRMED);
    }

    private void changeStateOfTransaction(String identifier, TransactionState newState) {
        TransactionView transactionView = transactionViewRepository.findOne(identifier);

        transactionView.setState(newState);

        transactionViewRepository.save(transactionView);
    }

    @EventHandler
    public void on(BuyTransactionExecutedEvent event) {
        executeTransaction(event);
    }

    @EventHandler
    public void on(SellTransactionExecutedEvent event) {
        executeTransaction(event);
    }

    private void executeTransaction(AbstractTransactionExecutedEvent event) {
        TransactionView transactionView = transactionViewRepository.findOne(event.getTransactionId().toString());

        long value = transactionView.getAmountOfExecutedItems() * transactionView.getPricePerItem();
        long additionalValue = event.getAmountOfItems() * event.getItemPrice();
        long newPrice = (value + additionalValue) / transactionView.getAmountOfItems();

        transactionView.setState(TransactionState.EXECUTED);
        transactionView.setAmountOfExecutedItems(transactionView.getAmountOfItems());
        transactionView.setPricePerItem(newPrice);

        transactionViewRepository.save(transactionView);
    }

    @EventHandler
    public void on(BuyTransactionPartiallyExecutedEvent event) {
        partiallyExecuteTransaction(event);
    }

    @EventHandler
    public void on(SellTransactionPartiallyExecutedEvent event) {
        partiallyExecuteTransaction(event);
    }

    private void partiallyExecuteTransaction(AbstractTransactionPartiallyExecutedEvent event) {
        TransactionView transactionView = transactionViewRepository.findOne(event.getTransactionId().toString());

        long value = transactionView.getAmountOfExecutedItems() * transactionView.getPricePerItem();
        long additionalValue = event.getAmountOfExecutedItems() * event.getItemPrice();
        long newPrice = (value + additionalValue) / event.getTotalOfExecutedItems();

        transactionView.setState(TransactionState.PARTIALLY_EXECUTED);
        transactionView.setAmountOfExecutedItems(event.getTotalOfExecutedItems());
        transactionView.setPricePerItem(newPrice);

        transactionViewRepository.save(transactionView);
    }
}
