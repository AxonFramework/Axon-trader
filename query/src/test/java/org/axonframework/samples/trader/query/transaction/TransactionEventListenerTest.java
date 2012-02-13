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

package org.axonframework.samples.trader.query.transaction;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.orders.api.transaction.BuyTransactionStartedEvent;
import org.axonframework.samples.trader.orders.api.transaction.SellTransactionCancelledEvent;
import org.axonframework.samples.trader.orders.api.transaction.SellTransactionStartedEvent;
import org.axonframework.samples.trader.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.transaction.repositories.TransactionQueryRepository;
import org.axonframework.test.utils.DomainEventUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.axonframework.samples.trader.orders.api.transaction.TransactionType.BUY;
import static org.axonframework.samples.trader.orders.api.transaction.TransactionType.SELL;
import static org.axonframework.samples.trader.query.transaction.TransactionState.CANCELLED;
import static org.axonframework.samples.trader.query.transaction.TransactionState.STARTED;


/**
 * @author Jettro Coenradie
 */
public class TransactionEventListenerTest {
    public static final AggregateIdentifier transactionIdentifier = new UUIDAggregateIdentifier();
    public static final AggregateIdentifier orderBookIdentifier = new UUIDAggregateIdentifier();
    public static final AggregateIdentifier portfolioIdentifier = new UUIDAggregateIdentifier();
    public static final AggregateIdentifier companyIdentifier = new UUIDAggregateIdentifier();

    public static final int DEFAULT_TOTAL_ITEMS = 100;
    public static final int DEFAULT_ITEM_PRICE = 10;
    private static final String DEFAULT_COMPANY_NAME = "Test Company";

    private TransactionEventListener listener;
    private TransactionQueryRepository transactionQueryRepository;

    @Before
    public void setUp() throws Exception {
        transactionQueryRepository = Mockito.mock(TransactionQueryRepository.class);
        OrderBookQueryRepository orderBookQueryRepository = Mockito.mock(OrderBookQueryRepository.class);

        listener = new TransactionEventListener();
        listener.setTransactionQueryRepository(transactionQueryRepository);
        listener.setOrderBookQueryRepository(orderBookQueryRepository);

        Mockito.when(orderBookQueryRepository.findOne(orderBookIdentifier.asString())).thenReturn(createOrderBookEntry());
    }

    @Test
    public void handleBuyTransactionStartedEvent() {
        BuyTransactionStartedEvent event = new BuyTransactionStartedEvent(orderBookIdentifier, portfolioIdentifier, DEFAULT_TOTAL_ITEMS, DEFAULT_ITEM_PRICE);
        DomainEventUtils.setAggregateIdentifier(event, transactionIdentifier);
        listener.handleEvent(event);

        Mockito.verify(transactionQueryRepository).save(Matchers.argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS,
                0,
                DEFAULT_COMPANY_NAME,
                DEFAULT_ITEM_PRICE,
                STARTED,
                BUY

        )));
    }

    @Test
    public void handleSellTransactionStartedEvent() {
        SellTransactionStartedEvent event = new SellTransactionStartedEvent(orderBookIdentifier, portfolioIdentifier, DEFAULT_TOTAL_ITEMS, DEFAULT_ITEM_PRICE);
        DomainEventUtils.setAggregateIdentifier(event, transactionIdentifier);
        listener.handleEvent(event);

        Mockito.verify(transactionQueryRepository).save(Matchers.argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS,
                0,
                DEFAULT_COMPANY_NAME,
                DEFAULT_ITEM_PRICE,
                STARTED,
                SELL

        )));
    }

    @Test
    public void handleSellTransactionCancelledEvent() {
        TransactionEntry transactionEntry = new TransactionEntry();
        transactionEntry.setIdentifier(transactionIdentifier.asString());
        transactionEntry.setAmountOfExecutedItems(0);
        transactionEntry.setPricePerItem(DEFAULT_ITEM_PRICE);
        transactionEntry.setState(STARTED);
        transactionEntry.setAmountOfItems(DEFAULT_TOTAL_ITEMS);
        transactionEntry.setCompanyName(DEFAULT_COMPANY_NAME);
        transactionEntry.setOrderbookIdentifier(orderBookIdentifier.asString());
        transactionEntry.setPortfolioIdentifier(portfolioIdentifier.asString());
        transactionEntry.setType(SELL);

        Mockito.when(transactionQueryRepository.findOne(transactionIdentifier.asString())).thenReturn(transactionEntry);
        SellTransactionCancelledEvent event = new SellTransactionCancelledEvent(DEFAULT_TOTAL_ITEMS, DEFAULT_TOTAL_ITEMS);
        DomainEventUtils.setAggregateIdentifier(event, transactionIdentifier);
        listener.handleEvent(event);
        Mockito.verify(transactionQueryRepository).save(Matchers.argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS,
                0,
                DEFAULT_COMPANY_NAME,
                DEFAULT_ITEM_PRICE,
                CANCELLED,
                SELL

        )));

    }

    private OrderBookEntry createOrderBookEntry() {
        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setIdentifier(orderBookIdentifier.asString());
        orderBookEntry.setCompanyIdentifier(companyIdentifier.asString());
        orderBookEntry.setCompanyName(DEFAULT_COMPANY_NAME);
        return orderBookEntry;
    }

}
