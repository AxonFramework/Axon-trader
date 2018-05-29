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

package org.axonframework.samples.trader.orders.command;

import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.samples.trader.api.orders.OrderId;
import org.axonframework.samples.trader.api.orders.trades.CreateSellOrderCommand;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.samples.trader.api.orders.transaction.*;
import org.axonframework.samples.trader.api.portfolio.cash.DepositCashCommand;
import org.axonframework.samples.trader.api.portfolio.stock.*;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Saga
public class SellTradeManagerSaga extends TradeManagerSaga {

    private static final Logger logger = LoggerFactory.getLogger(SellTradeManagerSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SellTransactionStartedEvent event) {
        transactionId = event.getTransactionId();
        orderBookId = event.getOrderBookId();
        portfolioId = event.getPortfolioId();
        pricePerItem = event.getPricePerItem();
        totalItems = event.getTotalItems();

        logger.debug(
                "A new sell transaction is started with identifier {}, for portfolio with identifier {} and order book with identifier {}",
                transactionId,
                portfolioId,
                orderBookId
        );
        logger.debug(
                "The sell transaction with identifier {} is for selling {} items for the price of {}",
                transactionId, totalItems, pricePerItem
        );

        commandGateway.send(new ReserveItemsCommand(portfolioId, orderBookId, transactionId, totalItems));
    }

    @SuppressWarnings("unused")
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(ItemsReservedEvent event) {
        logger.debug("Items for transaction {} are reserved", transactionId);

        commandGateway.send(new ConfirmTransactionCommand(transactionId));
    }

    @SuppressWarnings("unused")
    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(NotEnoughItemsAvailableToReserveInPortfolioEvent event) {
        logger.debug(
                "Cannot continue with transaction with id {} since the items needed cannot be reserved", totalItems
        );
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SellTransactionConfirmedEvent event) {
        logger.debug("Sell Transaction {} is approved to make the sell order", event.getTransactionId());

        commandGateway.send(new CreateSellOrderCommand(new OrderId(),
                                                       portfolioId,
                                                       orderBookId,
                                                       transactionId,
                                                       totalItems,
                                                       pricePerItem));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SellTransactionCancelledEvent event) {
        long amountOfCancelledItems = event.getTotalAmountOfItems() - event.getAmountOfExecutedItems();
        logger.debug(
                "Sell Transaction {} is cancelled, amount of cash reserved to cancel is {}",
                transactionId, amountOfCancelledItems
        );

        commandGateway.send(new CancelItemReservationForPortfolioCommand(portfolioId,
                                                                         orderBookId,
                                                                         transactionId,
                                                                         amountOfCancelledItems));
    }

    @SagaEventHandler(associationProperty = "sellTransactionId", keyName = "transactionId")
    public void handle(TradeExecutedEvent event) {
        long tradeCount = event.getTradeCount();
        long tradePrice = event.getTradePrice();

        logger.debug(
                "Sell Transaction {} is executed, items for transaction are {} for a price of {}",
                transactionId, tradeCount, tradePrice
        );
        commandGateway.send(new ExecutedTransactionCommand(transactionId, tradeCount, tradePrice));
    }


    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SellTransactionExecutedEvent event) {
        long amountOfItems = event.getAmountOfItems();
        long itemPrice = event.getItemPrice();

        logger.debug(
                "Sell Transaction {} is executed, last amount of executed items is {} for a price of {}",
                transactionId, amountOfItems, itemPrice
        );

        commandGateway.send(new ConfirmItemReservationForPortfolioCommand(portfolioId,
                                                                          orderBookId,
                                                                          transactionId,
                                                                          amountOfItems));
        commandGateway.send(new DepositCashCommand(portfolioId, itemPrice * amountOfItems));
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void handle(SellTransactionPartiallyExecutedEvent event) {
        long amountOfExecutedItems = event.getAmountOfExecutedItems();
        long itemPrice = event.getItemPrice();

        logger.debug(
                "Sell Transaction {} is partially executed, amount of executed items is {} for a price of {}",
                transactionId, amountOfExecutedItems, itemPrice
        );

        commandGateway.send(new ConfirmItemReservationForPortfolioCommand(portfolioId,
                                                                          orderBookId,
                                                                          transactionId,
                                                                          amountOfExecutedItems));
        commandGateway.send(new DepositCashCommand(portfolioId, itemPrice * amountOfExecutedItems));
    }
}
