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

package org.axonframework.samples.trader.app.query.portfolio;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemReservationCancelledForPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemReservationConfirmedForPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemsAddedToPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemsReservedEvent;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.app.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.app.query.portfolio.repositories.PortfolioQueryRepository;
import org.axonframework.test.utils.DomainEventUtils;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author Jettro Coenradie
 */
public class PortfolioItemEventListenerTest {
    public static final int DEFAULT_AMOUNT_ITEMS = 100;
    private PortfolioQueryRepository portfolioQueryRepository;
    private PortfolioItemEventListener listener;

    final AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
    final AggregateIdentifier itemIdentifier = new UUIDAggregateIdentifier();
    final AggregateIdentifier portfolioIdentifier = new UUIDAggregateIdentifier();
    final AggregateIdentifier companyIdentifier = new UUIDAggregateIdentifier();

    @Before
    public void setUp() throws Exception {
        portfolioQueryRepository = mock(PortfolioQueryRepository.class);

        OrderBookQueryRepository orderBookQueryRepository = mock(OrderBookQueryRepository.class);

        listener = new PortfolioItemEventListener();
        listener.setPortfolioRepository(portfolioQueryRepository);
        listener.setOrderBookQueryRepository(orderBookQueryRepository);

        OrderBookEntry orderBookEntry = createOrderBookEntry();
        when(orderBookQueryRepository.findOne(itemIdentifier.asString())).thenReturn(orderBookEntry);

        PortfolioEntry portfolioEntry = createPortfolioEntry();
        when(portfolioQueryRepository.findOne(portfolioIdentifier.asString())).thenReturn(portfolioEntry);

    }

    @Test
    public void testHandleEventAddItems() throws Exception {
        ItemsAddedToPortfolioEvent event = new ItemsAddedToPortfolioEvent(itemIdentifier, 100);
        DomainEventUtils.setAggregateIdentifier(event, portfolioIdentifier);
        listener.handleEvent(event);

        verify(portfolioQueryRepository).save(argThat(new PortfolioEntryMatcher(
                itemIdentifier.asString(),
                1,
                2 * DEFAULT_AMOUNT_ITEMS,
                1,
                DEFAULT_AMOUNT_ITEMS)));
    }

    @Test
    public void testHandleEventCancelItemReservation() throws Exception {
        ItemReservationCancelledForPortfolioEvent event =
                new ItemReservationCancelledForPortfolioEvent(itemIdentifier, DEFAULT_AMOUNT_ITEMS);
        DomainEventUtils.setAggregateIdentifier(event, portfolioIdentifier);
        listener.handleEvent(event);

        verify(portfolioQueryRepository).save(argThat(new PortfolioEntryMatcher(
                itemIdentifier.asString(),
                1,
                2 * DEFAULT_AMOUNT_ITEMS,
                0,
                0)));
    }

    @Test
    public void testHandleEventConfirmItemReservation() {
        ItemReservationConfirmedForPortfolioEvent event = new ItemReservationConfirmedForPortfolioEvent(itemIdentifier, 50);
        DomainEventUtils.setAggregateIdentifier(event, portfolioIdentifier);

        listener.handleEvent(event);

        verify(portfolioQueryRepository).save(argThat(new PortfolioEntryMatcher(
                itemIdentifier.asString(),
                1,
                DEFAULT_AMOUNT_ITEMS,
                1,
                DEFAULT_AMOUNT_ITEMS - 50)));
    }

    @Test
    public void testHandleItemReservedEvent() {
        ItemsReservedEvent event = new ItemsReservedEvent(itemIdentifier, DEFAULT_AMOUNT_ITEMS);
        DomainEventUtils.setAggregateIdentifier(event, portfolioIdentifier);
        listener.handleEvent(event);

        verify(portfolioQueryRepository).save(argThat(new PortfolioEntryMatcher(
                itemIdentifier.asString(),
                1,
                DEFAULT_AMOUNT_ITEMS,
                1,
                2 * DEFAULT_AMOUNT_ITEMS)));
    }

    private PortfolioEntry createPortfolioEntry() {
        PortfolioEntry portfolioEntry = new PortfolioEntry();
        portfolioEntry.setIdentifier(portfolioIdentifier.asString());
        portfolioEntry.setUserIdentifier(userIdentifier.asString());

        portfolioEntry.addItemInPossession(createItemEntry(itemIdentifier, companyIdentifier));
        portfolioEntry.addReservedItem(createItemEntry(itemIdentifier, companyIdentifier));
        portfolioEntry.setReservedAmountOfMoney(1000);
        portfolioEntry.setAmountOfMoney(10000);
        return portfolioEntry;
    }

    private OrderBookEntry createOrderBookEntry() {
        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setIdentifier(itemIdentifier.asString());
        orderBookEntry.setCompanyIdentifier(companyIdentifier.asString());
        orderBookEntry.setCompanyName("Test Company");
        return orderBookEntry;
    }

    private ItemEntry createItemEntry(AggregateIdentifier itemIdentifier, AggregateIdentifier companyIdentifier) {
        ItemEntry itemInPossession = new ItemEntry();
        itemInPossession.setIdentifier(itemIdentifier.asString());
        itemInPossession.setCompanyIdentifier(companyIdentifier.asString());
        itemInPossession.setCompanyName("Test company");
        itemInPossession.setAmount(DEFAULT_AMOUNT_ITEMS);
        return itemInPossession;
    }
}
