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

import org.axonframework.samples.trader.api.company.CompanyId;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.transaction.BuyTransactionStartedEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionCancelledEvent;
import org.axonframework.samples.trader.api.orders.transaction.SellTransactionStartedEvent;
import org.axonframework.samples.trader.api.orders.transaction.TransactionId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.transaction.repositories.TransactionQueryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.axonframework.samples.trader.api.orders.TransactionType.BUY;
import static org.axonframework.samples.trader.api.orders.TransactionType.SELL;
import static org.axonframework.samples.trader.query.transaction.TransactionState.CANCELLED;
import static org.axonframework.samples.trader.query.transaction.TransactionState.STARTED;

public class TransactionEventListenerTest {

    public static final TransactionId transactionIdentifier = new TransactionId();
    public static final OrderBookId orderBookIdentifier = new OrderBookId();
    public static final PortfolioId portfolioIdentifier = new PortfolioId();
    public static final CompanyId companyIdentifier = new CompanyId();

    public static final int DEFAULT_TOTAL_ITEMS = 100;
    public static final int DEFAULT_ITEM_PRICE = 10;
    private static final String DEFAULT_COMPANY_NAME = "Test Company";

    private TransactionEventListener listener;
    private TransactionQueryRepository transactionQueryRepository;

    @Before
    public void setUp() throws Exception {
        transactionQueryRepository = Mockito.mock(TransactionQueryRepository.class);
        OrderBookViewRepository orderBookViewRepository = Mockito.mock(OrderBookViewRepository.class);

        listener = new TransactionEventListener();
        listener.setTransactionQueryRepository(transactionQueryRepository);
        listener.setOrderBookViewRepository(orderBookViewRepository);

        Mockito.when(orderBookViewRepository.findOne(orderBookIdentifier.toString()))
               .thenReturn(createOrderBookEntry());
    }

    @Test
    public void handleBuyTransactionStartedEvent() {
        BuyTransactionStartedEvent event = new BuyTransactionStartedEvent(transactionIdentifier,
                                                                          orderBookIdentifier,
                                                                          portfolioIdentifier,
                                                                          DEFAULT_TOTAL_ITEMS,
                                                                          DEFAULT_ITEM_PRICE);
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
        SellTransactionStartedEvent event = new SellTransactionStartedEvent(transactionIdentifier,
                                                                            orderBookIdentifier,
                                                                            portfolioIdentifier,
                                                                            DEFAULT_TOTAL_ITEMS,
                                                                            DEFAULT_ITEM_PRICE);
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
        transactionEntry.setIdentifier(transactionIdentifier.toString());
        transactionEntry.setAmountOfExecutedItems(0);
        transactionEntry.setPricePerItem(DEFAULT_ITEM_PRICE);
        transactionEntry.setState(STARTED);
        transactionEntry.setAmountOfItems(DEFAULT_TOTAL_ITEMS);
        transactionEntry.setCompanyName(DEFAULT_COMPANY_NAME);
        transactionEntry.setOrderbookIdentifier(orderBookIdentifier.toString());
        transactionEntry.setPortfolioIdentifier(portfolioIdentifier.toString());
        transactionEntry.setType(SELL);

        Mockito.when(transactionQueryRepository.findOne(transactionIdentifier.toString())).thenReturn(transactionEntry);
        SellTransactionCancelledEvent event = new SellTransactionCancelledEvent(
                transactionIdentifier, DEFAULT_TOTAL_ITEMS, DEFAULT_TOTAL_ITEMS);
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

    private OrderBookView createOrderBookEntry() {
        OrderBookView orderBookView = new OrderBookView();
        orderBookView.setIdentifier(orderBookIdentifier.toString());
        orderBookView.setCompanyIdentifier(companyIdentifier.toString());
        orderBookView.setCompanyName(DEFAULT_COMPANY_NAME);
        return orderBookView;
    }
}
