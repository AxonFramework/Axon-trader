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

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.samples.trader.api.portfolio.stock.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.cash.*;
import org.axonframework.samples.trader.api.orders.transaction.*;
import org.axonframework.samples.trader.api.orders.trades.CreateBuyOrderCommand;
import org.axonframework.samples.trader.api.orders.trades.OrderId;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jettro Coenradie
 */
@Saga
public class BuyTradeManagerSaga extends TradeManagerSaga {

    private static final long serialVersionUID = 5948996680443725871L;
    private final static Logger logger = LoggerFactory.getLogger(BuyTradeManagerSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionStartedEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "A new buy transaction is started with identifier {}, for portfolio with identifier {} and orderbook with identifier {}",
                    event.getTransactionId(),
                    event.getPortfolioId(),
                    event.getOrderBookId());
            logger.debug("The new buy transaction with identifier {} is for buying {} items for the price of {}",
                         event.getTransactionId(),
                         event.getTotalItems(),
                         event.getPricePerItem());
        }
        setTransactionIdentifier(event.getTransactionId());
        setOrderbookIdentifier(event.getOrderBookId());
        setPortfolioIdentifier(event.getPortfolioId());
        setPricePerItem(event.getPricePerItem());
        setTotalItems(event.getTotalItems());

        ReserveCashCommand command = new ReserveCashCommand(getPortfolioIdentifier(),
                getTransactionIdentifier(),
                getTotalItems()
                        * getPricePerItem());
        getCommandBus().dispatch(new GenericCommandMessage<>(command));
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(CashReservedEvent event) {
        logger.debug("Money for transaction with identifier {} is reserved", getTransactionIdentifier());
        ConfirmTransactionCommand command = new ConfirmTransactionCommand(getTransactionIdentifier());
        getCommandBus().dispatch(new GenericCommandMessage<>(command),
                new CommandCallback<ConfirmTransactionCommand, Void>() {

                    @Override
                    public void onSuccess(CommandMessage commandMessage, Void result) {
                        // TODO jettro : Do we really need this?
                        logger.debug("Confirm transaction is dispatched successfully!");
                    }

                    @Override
                    public void onFailure(CommandMessage commandMessage, Throwable cause) {
                        logger.error("********* WOW!!!", cause);
                    }
                });
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    @EndSaga
    public void handle(CashReservationRejectedEvent event) {
        logger.debug(
                "Not enough cash was available to make reservation in transaction {} for portfolio {}. Required: {}",
                getTransactionIdentifier(),
                event.getPortfolioIdentifier(),
                event.getAmountToPayInCents());
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionConfirmedEvent event) {
        logger.debug("Buy Transaction {} is approved to make the buy order", event.getTransactionId());
        CreateBuyOrderCommand command = new CreateBuyOrderCommand(new OrderId(), getPortfolioIdentifier(),
                getOrderbookIdentifier(),
                getTransactionIdentifier(),
                getTotalItems(),
                getPricePerItem());
        getCommandBus().dispatch(new GenericCommandMessage<>(command));
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionCancelledEvent event) {
        long amountToCancel = (event.getTotalAmountOfItems() - event.getAmountOfExecutedItems()) * getPricePerItem();
        logger.debug("Buy Transaction {} is cancelled, amount of cash reserved to cancel is {}",
                event.getTransactionId(),
                amountToCancel);
        CancelCashReservationCommand command = new CancelCashReservationCommand(
                getPortfolioIdentifier(),
                getTransactionIdentifier(),
                amountToCancel);
        getCommandBus().dispatch(new GenericCommandMessage<>(command));
    }

    @SagaEventHandler(associationProperty = "buyTransactionId", keyName = "transactionIdentifier")
    public void handle(TradeExecutedEvent event) {
        logger.debug("Buy Transaction {} is executed, items for transaction are {} for a price of {}",
                     getTransactionIdentifier(), event.getTradeCount(), event.getTradePrice());
        ExecutedTransactionCommand command = new ExecutedTransactionCommand(getTransactionIdentifier(),
                event.getTradeCount(),
                event.getTradePrice());
        getCommandBus().dispatch(new GenericCommandMessage<>(command));
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    @EndSaga
    public void handle(BuyTransactionExecutedEvent event) {
        logger.debug("Buy Transaction {} is executed, last amount of executed items is {} for a price of {}",
                     event.getTransactionId(), event.getAmountOfItems(), event.getItemPrice());

        returnDifferenceInBidPriceAndExecutedPrice(getPricePerItem(), event.getItemPrice(), event.getAmountOfItems());

        ConfirmCashReservationCommand confirmCommand =
                new ConfirmCashReservationCommand(getPortfolioIdentifier(),
                        getTransactionIdentifier(),
                        event.getAmountOfItems() * event.getItemPrice());
        getCommandBus().dispatch(new GenericCommandMessage<>(confirmCommand));
        AddItemsToPortfolioCommand addItemsCommand =
                new AddItemsToPortfolioCommand(getPortfolioIdentifier(),
                        getOrderbookIdentifier(),
                        event.getAmountOfItems());
        getCommandBus().dispatch(new GenericCommandMessage<>(addItemsCommand));
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionPartiallyExecutedEvent event) {
        logger.debug("Buy Transaction {} is partially executed, amount of executed items is {} for a price of {}",
                     event.getTransactionId(),
                     event.getAmountOfExecutedItems(),
                     event.getItemPrice());

        returnDifferenceInBidPriceAndExecutedPrice(getPricePerItem(),
                                                   event.getItemPrice(),
                                                   event.getAmountOfExecutedItems());

        ConfirmCashReservationCommand confirmCommand =
                new ConfirmCashReservationCommand(getPortfolioIdentifier(),
                        getTransactionIdentifier(),
                        event.getAmountOfExecutedItems() * event
                                .getItemPrice());
        getCommandBus().dispatch(new GenericCommandMessage<>(confirmCommand));
        AddItemsToPortfolioCommand addItemsCommand =
                new AddItemsToPortfolioCommand(getPortfolioIdentifier(),
                        getOrderbookIdentifier(),
                        event.getAmountOfExecutedItems());
        getCommandBus().dispatch(new GenericCommandMessage<>(addItemsCommand));
    }

    private void returnDifferenceInBidPriceAndExecutedPrice(long bidPrice, long executedPrice,
                                                            long amountOfExecutedItems) {
        long totalDifferenceInCents = amountOfExecutedItems * bidPrice - amountOfExecutedItems * executedPrice;

        if (totalDifferenceInCents > 0) {
            CancelCashReservationCommand cancelCashReservationCommand = new CancelCashReservationCommand(
                    getPortfolioIdentifier(),
                    getTransactionIdentifier(),
                    totalDifferenceInCents);
            getCommandBus().dispatch(new GenericCommandMessage<>(
                    cancelCashReservationCommand));
        }
    }

}
