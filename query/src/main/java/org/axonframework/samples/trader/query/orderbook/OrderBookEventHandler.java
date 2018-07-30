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

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.company.OrderBookAddedToCompanyEvent;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.OrderId;
import org.axonframework.samples.trader.api.orders.trades.AbstractOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.BuyOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.SellOrderPlacedEvent;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.query.company.CompanyView;
import org.axonframework.samples.trader.query.company.repositories.CompanyViewRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.tradeexecuted.TradeExecutedView;
import org.axonframework.samples.trader.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("queryModel")
public class OrderBookEventHandler {

    private static final String BUY = "Buy";
    private static final String SELL = "Sell";

    private final OrderBookViewRepository orderBookRepository;
    private final CompanyViewRepository companyRepository;
    private final TradeExecutedQueryRepository tradeExecutedRepository;

    @Autowired
    public OrderBookEventHandler(OrderBookViewRepository orderBookRepository,
                                 CompanyViewRepository companyRepository,
                                 TradeExecutedQueryRepository tradeExecutedRepository) {
        this.orderBookRepository = orderBookRepository;
        this.companyRepository = companyRepository;
        this.tradeExecutedRepository = tradeExecutedRepository;
    }

    @EventHandler
    public void on(OrderBookAddedToCompanyEvent event) {
        CompanyView companyView = companyRepository.findOne(event.getCompanyId().toString());

        OrderBookView orderBookView = new OrderBookView();
        orderBookView.setCompanyIdentifier(event.getCompanyId().toString());
        orderBookView.setCompanyName(companyView.getName());
        orderBookView.setIdentifier(event.getOrderBookId().toString());

        orderBookRepository.save(orderBookView);
    }

    @EventHandler
    public void on(BuyOrderPlacedEvent event) {
        OrderBookView orderBook = orderBookRepository.findOne(event.getOrderBookId().toString());

        OrderView buyOrder = createPlacedOrder(event, BUY);
        orderBook.buyOrders().add(buyOrder);

        orderBookRepository.save(orderBook);
    }

    @EventHandler
    public void on(SellOrderPlacedEvent event) {
        OrderBookView orderBook = orderBookRepository.findOne(event.getOrderBookId().toString());

        OrderView sellOrder = createPlacedOrder(event, SELL);
        orderBook.sellOrders().add(sellOrder);

        orderBookRepository.save(orderBook);
    }

    @EventHandler
    public void on(TradeExecutedEvent event) {
        OrderId buyOrderId = event.getBuyOrderId();
        OrderId sellOrderId = event.getSellOrderId();

        OrderBookId orderBookIdentifier = event.getOrderBookId();
        OrderBookView orderBookView = orderBookRepository.findOne(orderBookIdentifier.toString());

        TradeExecutedView tradeExecutedView = new TradeExecutedView();
        tradeExecutedView.setCompanyName(orderBookView.getCompanyName());
        tradeExecutedView.setOrderBookId(orderBookView.getIdentifier());
        tradeExecutedView.setTradeCount(event.getTradeCount());
        tradeExecutedView.setTradePrice(event.getTradePrice());

        tradeExecutedRepository.save(tradeExecutedView);

        OrderView foundBuyOrder = null;
        for (OrderView order : orderBookView.buyOrders()) {
            if (order.getIdentifier().equals(buyOrderId.toString())) {
                long itemsRemaining = order.getItemsRemaining();
                order.setItemsRemaining(itemsRemaining - event.getTradeCount());
                foundBuyOrder = order;
                break;
            }
        }
        if (null != foundBuyOrder && foundBuyOrder.getItemsRemaining() == 0) {
            orderBookView.buyOrders().remove(foundBuyOrder);
        }
        OrderView foundSellOrder = null;
        for (OrderView order : orderBookView.sellOrders()) {
            if (order.getIdentifier().equals(sellOrderId.toString())) {
                long itemsRemaining = order.getItemsRemaining();
                order.setItemsRemaining(itemsRemaining - event.getTradeCount());
                foundSellOrder = order;
                break;
            }
        }
        if (null != foundSellOrder && foundSellOrder.getItemsRemaining() == 0) {
            orderBookView.sellOrders().remove(foundSellOrder);
        }
        orderBookRepository.save(orderBookView);
    }

    private OrderView createPlacedOrder(AbstractOrderPlacedEvent event, String type) {
        OrderView entry = new OrderView();

        entry.setIdentifier(event.getOrderId().toString());
        entry.setItemsRemaining(event.getTradeCount());
        entry.setTradeCount(event.getTradeCount());
        entry.setUserId(event.getPortfolioId().toString());
        entry.setType(type);
        entry.setItemPrice(event.getItemPrice());

        return entry;
    }
}
