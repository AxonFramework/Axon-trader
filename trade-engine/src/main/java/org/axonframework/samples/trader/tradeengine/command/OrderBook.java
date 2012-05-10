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

package org.axonframework.samples.trader.tradeengine.command;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.AbstractEventSourcedEntity;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.tradeengine.api.order.*;

import java.util.*;

/**
 * @author Allard Buijze
 */
class OrderBook extends AbstractAnnotatedAggregateRoot {
    private static final long serialVersionUID = 6778782949492587631L;

    private OrderBookId orderBookId;
    private SortedSet<Order> buyOrders = new TreeSet<Order>(new OrderComparator());
    private SortedSet<Order> sellOrders = new TreeSet<Order>(new OrderComparator());

    public OrderBook(OrderBookId identifier) {
        this.orderBookId = identifier;
        apply(new OrderBookCreatedEvent(identifier));
    }

    @Override
    public OrderBookId getIdentifier() {
        return orderBookId;
    }

    public void addBuyOrder(OrderId orderId, TransactionId transactionId, long tradeCount,
                            long itemPrice, PortfolioId portfolioId) {
        apply(new BuyOrderPlacedEvent(orderBookId, orderId, transactionId, tradeCount, itemPrice, portfolioId));
        executeTrades();
    }

    public void addSellOrder(OrderId orderId, TransactionId transactionId, long tradeCount,
                             long itemPrice, PortfolioId portfolioId) {
        apply(new SellOrderPlacedEvent(orderBookId, orderId, transactionId, tradeCount, itemPrice, portfolioId));
        executeTrades();
    }

    private void executeTrades() {
        boolean tradingDone = false;
        while (!tradingDone && !buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order highestBuyer = buyOrders.last();
            Order lowestSeller = sellOrders.first();
            if (highestBuyer.getItemPrice() >= lowestSeller.getItemPrice()) {
                long matchedTradeCount = Math.min(highestBuyer.getItemsRemaining(), lowestSeller.getItemsRemaining());
                long matchedTradePrice = ((highestBuyer.getItemPrice() + lowestSeller.getItemPrice()) / 2);
                apply(new TradeExecutedEvent(orderBookId,
                        matchedTradeCount,
                        matchedTradePrice,
                        highestBuyer.getOrderId(),
                        lowestSeller.getOrderId(),
                        highestBuyer.getTransactionId(),
                        lowestSeller.getTransactionId()));
            } else {
                tradingDone = true;
            }
        }
    }

    @EventHandler
    protected void onBuyPlaced(BuyOrderPlacedEvent event) {
        buyOrders.add(new Order(event.getOrderId(),
                event.getTransactionIdentifier(),
                event.getItemPrice(),
                event.getTradeCount(),
                event.getPortfolioId()));
    }

    @EventHandler
    protected void onSellPlaced(SellOrderPlacedEvent event) {
        sellOrders.add(new Order(event.getOrderId(),
                event.getTransactionIdentifier(),
                event.getItemPrice(),
                event.getTradeCount(),
                event.getPortfolioId()));
    }

    @EventHandler
    protected void onTradeExecuted(TradeExecutedEvent event) {
        Order highestBuyer = buyOrders.last();
        Order lowestSeller = sellOrders.first();
        if (highestBuyer.getItemsRemaining() <= event.getTradeCount()) {
            buyOrders.remove(highestBuyer);
        }
        if (lowestSeller.getItemsRemaining() <= event.getTradeCount()) {
            sellOrders.remove(lowestSeller);
        }
    }

    @Override
    protected Collection<AbstractEventSourcedEntity> getChildEntities() {
        List<AbstractEventSourcedEntity> children = new ArrayList<AbstractEventSourcedEntity>(
                buyOrders.size() + sellOrders.size());
        children.addAll(buyOrders);
        children.addAll(sellOrders);
        return children;
    }

    private static class OrderComparator implements Comparator<Order> {

        public int compare(Order o1, Order o2) {
            if (o1.getItemPrice() == o2.getItemPrice()) {
                return 0;
            }

            if (o1.getItemPrice() > o2.getItemPrice()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
