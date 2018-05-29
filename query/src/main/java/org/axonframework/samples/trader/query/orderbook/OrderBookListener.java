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

package org.axonframework.samples.trader.query.orderbook;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.company.OrderBookAddedToCompanyEvent;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.OrderId;
import org.axonframework.samples.trader.api.orders.trades.AbstractOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.SellOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.query.company.CompanyEntry;
import org.axonframework.samples.trader.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderBookListener {

    private static final String BUY = "Buy";
    private static final String SELL = "Sell";

    private OrderBookQueryRepository orderBookRepository;
    private CompanyQueryRepository companyRepository;
    private TradeExecutedQueryRepository tradeExecutedRepository;


    @EventHandler
    public void handleOrderBookAddedToCompanyEvent(OrderBookAddedToCompanyEvent event) {
        CompanyEntry companyEntry = companyRepository.findOne(event.getCompanyId().toString());
        OrderBookEntry orderBookEntry = new OrderBookEntry();
        orderBookEntry.setCompanyIdentifier(event.getCompanyId().toString());
        orderBookEntry.setCompanyName(companyEntry.getName());
        orderBookEntry.setIdentifier(event.getOrderBookId().toString());
        orderBookRepository.save(orderBookEntry);
    }

    @EventHandler
    public void handleBuyOrderPlaced(BuyOrderPlacedEvent event) {
        OrderBookEntry orderBook = orderBookRepository.findOne(event.getOrderBookId().toString());

        OrderEntry buyOrder = createPlacedOrder(event, BUY);
        orderBook.buyOrders().add(buyOrder);

        orderBookRepository.save(orderBook);
    }

    @EventHandler
    public void handleSellOrderPlaced(SellOrderPlacedEvent event) {
        OrderBookEntry orderBook = orderBookRepository.findOne(event.getOrderBookId().toString());

        OrderEntry sellOrder = createPlacedOrder(event, SELL);
        orderBook.sellOrders().add(sellOrder);

        orderBookRepository.save(orderBook);
    }

    @EventHandler
    public void handleTradeExecuted(TradeExecutedEvent event) {
        OrderId buyOrderId = event.getBuyOrderId();
        OrderId sellOrderId = event.getSellOrderId();

        OrderBookId orderBookIdentifier = event.getOrderBookId();
        OrderBookEntry orderBookEntry = orderBookRepository.findOne(orderBookIdentifier.toString());

        TradeExecutedEntry tradeExecutedEntry = new TradeExecutedEntry();
        tradeExecutedEntry.setCompanyName(orderBookEntry.getCompanyName());
        tradeExecutedEntry.setOrderBookIdentifier(orderBookEntry.getIdentifier());
        tradeExecutedEntry.setTradeCount(event.getTradeCount());
        tradeExecutedEntry.setTradePrice(event.getTradePrice());

        tradeExecutedRepository.save(tradeExecutedEntry);

        // TODO find a better solution or maybe pull them apart
        OrderEntry foundBuyOrder = null;
        for (OrderEntry order : orderBookEntry.buyOrders()) {
            if (order.getIdentifier().equals(buyOrderId.toString())) {
                long itemsRemaining = order.getItemsRemaining();
                order.setItemsRemaining(itemsRemaining - event.getTradeCount());
                foundBuyOrder = order;
                break;
            }
        }
        if (null != foundBuyOrder && foundBuyOrder.getItemsRemaining() == 0) {
            orderBookEntry.buyOrders().remove(foundBuyOrder);
        }
        OrderEntry foundSellOrder = null;
        for (OrderEntry order : orderBookEntry.sellOrders()) {
            if (order.getIdentifier().equals(sellOrderId.toString())) {
                long itemsRemaining = order.getItemsRemaining();
                order.setItemsRemaining(itemsRemaining - event.getTradeCount());
                foundSellOrder = order;
                break;
            }
        }
        if (null != foundSellOrder && foundSellOrder.getItemsRemaining() == 0) {
            orderBookEntry.sellOrders().remove(foundSellOrder);
        }
        orderBookRepository.save(orderBookEntry);
    }

    private OrderEntry createPlacedOrder(AbstractOrderPlacedEvent event, String type) {
        OrderEntry entry = new OrderEntry();
        entry.setIdentifier(event.getOrderId().toString());
        entry.setItemsRemaining(event.getTradeCount());
        entry.setTradeCount(event.getTradeCount());
        entry.setUserId(event.getPortfolioId().toString());
        entry.setType(type);
        entry.setItemPrice(event.getItemPrice());

        return entry;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setOrderBookRepository(OrderBookQueryRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setCompanyRepository(CompanyQueryRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setTradeExecutedRepository(TradeExecutedQueryRepository tradeExecutedRepository) {
        this.tradeExecutedRepository = tradeExecutedRepository;
    }
}
