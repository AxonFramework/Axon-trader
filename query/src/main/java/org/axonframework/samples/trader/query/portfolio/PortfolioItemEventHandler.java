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
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.portfolio.stock.ItemReservationCancelledForPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemReservationConfirmedForPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemsAddedToPortfolioEvent;
import org.axonframework.samples.trader.api.portfolio.stock.ItemsReservedEvent;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("queryModel")
public class PortfolioItemEventHandler {

    private final static Logger logger = LoggerFactory.getLogger(PortfolioItemEventHandler.class);

    private final PortfolioViewRepository portfolioViewRepository;
    private final OrderBookViewRepository orderBookViewRepository;

    @Autowired
    public PortfolioItemEventHandler(PortfolioViewRepository portfolioViewRepository,
                                     OrderBookViewRepository orderBookViewRepository) {
        this.portfolioViewRepository = portfolioViewRepository;
        this.orderBookViewRepository = orderBookViewRepository;
    }

    @EventHandler
    public void on(ItemsAddedToPortfolioEvent event) {
        OrderBookId orderBookId = event.getOrderBookId();
        logger.debug("Handle ItemsAddedToPortfolioEvent for orderBook with identifier {}", orderBookId);

        ItemEntry itemEntry = createItemEntry(orderBookId.toString(), event.getAmountOfItemsAdded());

        PortfolioView portfolioView = portfolioViewRepository.findOne(event.getPortfolioId().toString());
        portfolioView.addItemInPossession(itemEntry);

        portfolioViewRepository.save(portfolioView);
    }

    @EventHandler
    public void on(ItemReservationCancelledForPortfolioEvent event) {
        OrderBookId orderBookId = event.getOrderBookId();
        logger.debug("Handle ItemReservationCancelledForPortfolioEvent for orderBook with identifier {}", orderBookId);

        ItemEntry itemEntry = createItemEntry(orderBookId.toString(), event.getAmountOfCancelledItems());

        PortfolioView portfolioView = portfolioViewRepository.findOne(event.getPortfolioId().toString());
        portfolioView.removeReservedItem(orderBookId.toString(), event.getAmountOfCancelledItems());
        portfolioView.addItemInPossession(itemEntry);

        portfolioViewRepository.save(portfolioView);
    }

    @EventHandler
    public void on(ItemReservationConfirmedForPortfolioEvent event) {
        OrderBookId orderBookId = event.getOrderBookId();
        logger.debug("Handle ItemReservationConfirmedForPortfolioEvent for orderBook with identifier {}", orderBookId);

        PortfolioView portfolioView = portfolioViewRepository.findOne(event.getPortfolioId().toString());

        portfolioView.removeReservedItem(orderBookId.toString(), event.getAmountOfConfirmedItems());
        portfolioView.removeItemsInPossession(orderBookId.toString(), event.getAmountOfConfirmedItems());

        portfolioViewRepository.save(portfolioView);
    }

    @EventHandler
    public void on(ItemsReservedEvent event) {
        OrderBookId orderBookId = event.getOrderBookId();
        logger.debug("Handle ItemsReservedEvent for orderBook with identifier {}", orderBookId);

        ItemEntry itemEntry = createItemEntry(orderBookId.toString(), event.getAmountOfItemsReserved());

        PortfolioView portfolioView = portfolioViewRepository.findOne(event.getPortfolioId().toString());
        portfolioView.addReservedItem(itemEntry);

        portfolioViewRepository.save(portfolioView);
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
}
