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
import org.axonframework.samples.trader.query.orderbook.OrderBookViewRepository;
import org.junit.*;

import static org.axonframework.samples.trader.api.orders.TransactionType.BUY;
import static org.axonframework.samples.trader.api.orders.TransactionType.SELL;
import static org.axonframework.samples.trader.query.transaction.TransactionState.CANCELLED;
import static org.axonframework.samples.trader.query.transaction.TransactionState.STARTED;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class TransactionEventHandlerTest {

    private static final int DEFAULT_TOTAL_ITEMS = 100;
    private static final int DEFAULT_ITEM_PRICE = 10;
    private static final String DEFAULT_COMPANY_NAME = "Test Company";

    private final OrderBookViewRepository orderBookViewRepository = mock(OrderBookViewRepository.class);
    private final TransactionViewRepository transactionViewRepository = mock(TransactionViewRepository.class);

    private TransactionEventHandler testSubject;

    private final TransactionId transactionIdentifier = new TransactionId();
    private final OrderBookId orderBookIdentifier = new OrderBookId();
    private final PortfolioId portfolioIdentifier = new PortfolioId();
    private final CompanyId companyIdentifier = new CompanyId();

    @Before
    public void setUp() {
        when(orderBookViewRepository.findOne(orderBookIdentifier.getIdentifier())).thenReturn(createOrderBookEntry());

        testSubject = new TransactionEventHandler(orderBookViewRepository, transactionViewRepository);
    }

    @Test
    public void handleBuyTransactionStartedEvent() {
        testSubject.on(new BuyTransactionStartedEvent(transactionIdentifier,
                                                      orderBookIdentifier,
                                                      portfolioIdentifier,
                                                      DEFAULT_TOTAL_ITEMS,
                                                      DEFAULT_ITEM_PRICE));

        verify(transactionViewRepository).save(argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS, 0, DEFAULT_COMPANY_NAME, DEFAULT_ITEM_PRICE, STARTED, BUY
        )));
    }

    @Test
    public void handleSellTransactionStartedEvent() {
        testSubject.on(new SellTransactionStartedEvent(transactionIdentifier,
                                                       orderBookIdentifier,
                                                       portfolioIdentifier,
                                                       DEFAULT_TOTAL_ITEMS,
                                                       DEFAULT_ITEM_PRICE));

        verify(transactionViewRepository).save(argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS, 0, DEFAULT_COMPANY_NAME, DEFAULT_ITEM_PRICE, STARTED, SELL
        )));
    }

    @Test
    public void handleSellTransactionCancelledEvent() {
        TransactionView transactionView = new TransactionView();
        transactionView.setIdentifier(transactionIdentifier.getIdentifier());
        transactionView.setAmountOfExecutedItems(0);
        transactionView.setPricePerItem(DEFAULT_ITEM_PRICE);
        transactionView.setState(STARTED);
        transactionView.setAmountOfItems(DEFAULT_TOTAL_ITEMS);
        transactionView.setCompanyName(DEFAULT_COMPANY_NAME);
        transactionView.setOrderBookId(orderBookIdentifier.getIdentifier());
        transactionView.setPortfolioId(portfolioIdentifier.getIdentifier());
        transactionView.setType(SELL);

        when(transactionViewRepository.findOne(transactionIdentifier.getIdentifier())).thenReturn(transactionView);

        testSubject.on(new SellTransactionCancelledEvent(transactionIdentifier,
                                                         DEFAULT_TOTAL_ITEMS,
                                                         DEFAULT_TOTAL_ITEMS));

        verify(transactionViewRepository).save(argThat(new TransactionEntryMatcher(
                DEFAULT_TOTAL_ITEMS, 0, DEFAULT_COMPANY_NAME, DEFAULT_ITEM_PRICE, CANCELLED, SELL
        )));
    }

    private OrderBookView createOrderBookEntry() {
        OrderBookView orderBookView = new OrderBookView();

        orderBookView.setIdentifier(orderBookIdentifier.getIdentifier());
        orderBookView.setCompanyIdentifier(companyIdentifier.getIdentifier());
        orderBookView.setCompanyName(DEFAULT_COMPANY_NAME);

        return orderBookView;
    }
}
