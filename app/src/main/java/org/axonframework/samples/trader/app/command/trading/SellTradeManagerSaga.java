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

package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.saga.annotation.EndSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.axonframework.samples.trader.app.api.order.CreateSellOrderCommand;
import org.axonframework.samples.trader.app.api.portfolio.item.*;
import org.axonframework.samples.trader.app.api.portfolio.money.DepositMoneyToPortfolioCommand;
import org.axonframework.samples.trader.app.api.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jettro Coenradie
 */
public class SellTradeManagerSaga extends TradeManagerSaga {
    private final static Logger logger = LoggerFactory.getLogger(SellTradeManagerSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(SellTransactionStartedEvent event) {
        setTransactionIdentifier(event.getTransactionIdentifier());
        setOrderbookIdentifier(event.getOrderbookIdentifier());
        setPortfolioIdentifier(event.getPortfolioIdentifier());
        setPricePerItem(event.getPricePerItem());
        setTotalItems(event.getTotalItems());

        associateWith("orderBookIdentifier", getOrderbookIdentifier());
        associateWith("portfolioIdentifier", getPortfolioIdentifier());

        ReserveItemsCommand reserveItemsCommand =
                new ReserveItemsCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getTotalItems());
        getCommandBus().dispatch(reserveItemsCommand);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    public void handle(ItemsReservedEvent event) {
        ConfirmTransactionCommand confirmTransactionCommand = new ConfirmTransactionCommand(getTransactionIdentifier());
        getCommandBus().dispatch(confirmTransactionCommand);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    @EndSaga
    public void handle(NotEnoughItemsAvailableToReserveInPortfolio event) {
        logger.debug("Cannot continue with transaction with id {} since the items needed cannot be reserved", getTotalItems());
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(SellTransactionConfirmedEvent event) {
        CreateSellOrderCommand command = new CreateSellOrderCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), getTotalItems(), getPricePerItem());
        getCommandBus().dispatch(command);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    @EndSaga
    public void handle(SellTransactionCancelledEvent event) {
        logger.debug("Saga for Sell Transaction with id {} is cancelled", event.getTransactionIdentifier());
        CancelItemReservationForPortfolioCommand command =
                new CancelItemReservationForPortfolioCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getTotalAmountOfItems() - event.getAmountOfExecutedItems());
        getCommandBus().dispatch(command);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    @EndSaga
    public void handle(SellTransactionExecutedEvent event) {
        ConfirmItemReservationForPortfolioCommand confirmCommand =
                new ConfirmItemReservationForPortfolioCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getAmountOfItems());
        getCommandBus().dispatch(confirmCommand);
        DepositMoneyToPortfolioCommand depositCommand =
                new DepositMoneyToPortfolioCommand(getPortfolioIdentifier(), event.getItemPrice() * event.getAmountOfItems());
        getCommandBus().dispatch(depositCommand);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(SellTransactionPartiallyExecutedEvent event) {
        ConfirmItemReservationForPortfolioCommand confirmCommand =
                new ConfirmItemReservationForPortfolioCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getAmountOfExecutedItems());
        getCommandBus().dispatch(confirmCommand);
        DepositMoneyToPortfolioCommand depositCommand =
                new DepositMoneyToPortfolioCommand(getPortfolioIdentifier(), event.getItemPrice() * event.getAmountOfExecutedItems());
        getCommandBus().dispatch(depositCommand);
    }

}
