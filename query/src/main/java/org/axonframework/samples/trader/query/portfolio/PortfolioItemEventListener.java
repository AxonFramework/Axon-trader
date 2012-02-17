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

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.orders.api.portfolio.item.ItemReservationCancelledForPortfolioEvent;
import org.axonframework.samples.trader.orders.api.portfolio.item.ItemReservationConfirmedForPortfolioEvent;
import org.axonframework.samples.trader.orders.api.portfolio.item.ItemsAddedToPortfolioEvent;
import org.axonframework.samples.trader.orders.api.portfolio.item.ItemsReservedEvent;
import org.axonframework.samples.trader.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class PortfolioItemEventListener {

    private final static Logger logger = LoggerFactory.getLogger(PortfolioItemEventListener.class);
    private PortfolioQueryRepository portfolioRepository;
    private OrderBookQueryRepository orderBookQueryRepository;

    @EventHandler
    public void handleEvent(ItemsAddedToPortfolioEvent event) {
        logger.debug("Handle ItemsAddedToPortfolioEvent for orderbook with identifier {}",
                     event.getOrderBookIdentifier());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookIdentifier().asString(), event.getAmountOfItemsAdded());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.addItemInPossession(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemReservationCancelledForPortfolioEvent event) {
        logger.debug("Handle ItemReservationCancelledForPortfolioEvent for orderbook with identifier {}",
                     event.getOrderBookIdentifier());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookIdentifier().asString(),
                                              event.getAmountOfCancelledItems());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.removeReservedItem(event.getOrderBookIdentifier().asString(), event.getAmountOfCancelledItems());
        portfolioEntry.addItemInPossession(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemReservationConfirmedForPortfolioEvent event) {
        logger.debug("Handle ItemReservationConfirmedForPortfolioEvent for orderbook with identifier {}",
                     event.getOrderBookIdentifier());
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.removeReservedItem(event.getOrderBookIdentifier().asString(), event.getAmountOfConfirmedItems());
        portfolioEntry.removeItemsInPossession(event.getOrderBookIdentifier().asString(),
                                               event.getAmountOfConfirmedItems());

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(ItemsReservedEvent event) {
        logger.debug("Handle ItemsReservedEvent for orderbook with identifier {}", event.getOrderBookIdentifier());
        ItemEntry itemEntry = createItemEntry(event.getOrderBookIdentifier().asString(),
                                              event.getAmountOfItemsReserved());

        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.addReservedItem(itemEntry);

        portfolioRepository.save(portfolioEntry);
    }

    private ItemEntry createItemEntry(String identifier, long amount) {
        OrderBookEntry orderBookEntry = orderBookQueryRepository.findOne(identifier);
        ItemEntry itemEntry = new ItemEntry();
        itemEntry.setIdentifier(identifier);
        itemEntry.setCompanyIdentifier(orderBookEntry.getCompanyIdentifier());
        itemEntry.setCompanyName(orderBookEntry.getCompanyName());
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
    public void setOrderBookQueryRepository(OrderBookQueryRepository orderBookQueryRepository) {
        this.orderBookQueryRepository = orderBookQueryRepository;
    }
}
