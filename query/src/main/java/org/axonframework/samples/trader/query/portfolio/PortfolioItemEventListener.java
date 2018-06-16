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

package org.axonframework.samples.trader.query.portfolio;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.portfolio.stock.ItemReservationCancelledForPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemReservationConfirmedForPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemsAddedToPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemsReservedEvent;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("queryModel")
public class PortfolioItemEventListener {

    private final static Logger logger = LoggerFactory.getLogger(PortfolioItemEventListener.class);
    private PortfolioQueryRepository portfolioRepository;
    private OrderBookViewRepository orderBookViewRepository;

    @EventHandler
    public void handleEvent(ItemsAddedToPortfolioEvent event) {
        logger.debug("Handle ItemsAddedToPortfolioEvent for orderbook with identifier {}",
                event.getOrderBookId());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookId().toString(), event.getAmountOfItemsAdded());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioId().toString());
        portfolioEntry.addItemInPossession(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemReservationCancelledForPortfolioEvent event) {
        logger.debug("Handle ItemReservationCancelledForPortfolioEvent for orderbook with identifier {}",
                event.getOrderBookId());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookId().toString(),
                event.getAmountOfCancelledItems());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioId().toString());
        portfolioEntry.removeReservedItem(event.getOrderBookId().toString(), event.getAmountOfCancelledItems());
        portfolioEntry.addItemInPossession(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemReservationConfirmedForPortfolioEvent event) {
        logger.debug("Handle ItemReservationConfirmedForPortfolioEvent for orderbook with identifier {}",
                event.getOrderBookId());
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioId().toString());
        portfolioEntry.removeReservedItem(event.getOrderBookId().toString(), event.getAmountOfConfirmedItems());
        portfolioEntry.removeItemsInPossession(event.getOrderBookId().toString(),
                event.getAmountOfConfirmedItems());

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemsReservedEvent event) {
        logger.debug("Handle ItemsReservedEvent for orderbook with identifier {}", event.getOrderBookId());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookId().toString(),
                event.getAmountOfItemsReserved());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioId().toString());
        portfolioEntry.addReservedItem(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    private ItemEntry createItemEntry(String identifier, long amount) {
        OrderBookView orderBookView = orderBookViewRepository.findOne(identifier);
        ItemEntry itemEntry = new ItemEntry();
        itemEntry.setIdentifier(identifier);
        itemEntry.setCompanyIdentifier(orderBookView.getCompanyIdentifier());
        itemEntry.setCompanyName(orderBookView.getCompanyName());
        itemEntry.setAmount(amount);
        return itemEntry;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setPortfolioRepository(PortfolioQueryRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setOrderBookViewRepository(OrderBookViewRepository orderBookViewRepository) {
        this.orderBookViewRepository = orderBookViewRepository;
    }
}
