/*
 * Copyright (c) 2012. Gridshore
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
import org.axonframework.samples.trader.app.api.order.CreateBuyOrderCommand;
import org.axonframework.samples.trader.app.api.portfolio.item.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.app.api.portfolio.money.*;
import org.axonframework.samples.trader.app.api.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jettro Coenradie
 */
public class BuyTradeManagerSaga extends TradeManagerSaga {
    private final static Logger logger = LoggerFactory.getLogger(BuyTradeManagerSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionStartedEvent event) {
        setTransactionIdentifier(event.getTransactionIdentifier());
        setOrderbookIdentifier(event.getOrderbookIdentifier());
        setPortfolioIdentifier(event.getPortfolioIdentifier());

        associateWith("orderBookIdentifier", getOrderbookIdentifier());
        associateWith("portfolioIdentifier", getPortfolioIdentifier());

        setPricePerItem(event.getPricePerItem());
        setTotalItems(event.getTotalItems());

        ReserveMoneyFromPortfolioCommand command = new ReserveMoneyFromPortfolioCommand(getPortfolioIdentifier(), getTotalItems() * getPricePerItem());
        getCommandBus().dispatch(command);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    public void handle(MoneyReservedFromPortfolioEvent event) {
        ConfirmTransactionCommand command = new ConfirmTransactionCommand(getTransactionIdentifier());
        getCommandBus().dispatch(command);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    @EndSaga
    public void handle(NotEnoughMoneyInPortfolioToMakeReservationEvent event) {
        // Nothing to do, no reservations were made and the transaction is finished
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionConfirmedEvent event) {
        CreateBuyOrderCommand command = new CreateBuyOrderCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), getTotalItems(), getPricePerItem());
        getCommandBus().dispatch(command);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionCancelledEvent event) {
        long amountToCancel = (event.getTotalAmountOfItems() - event.getAmountOfExecutedItems()) * getPricePerItem();
        CancelMoneyReservationFromPortfolioCommand command = new CancelMoneyReservationFromPortfolioCommand(getPortfolioIdentifier(), amountToCancel);
        getCommandBus().dispatch(command);
    }


    @SagaEventHandler(associationProperty = "transactionIdentifier")
    @EndSaga
    public void handle(BuyTransactionExecutedEvent event) {
        ConfirmMoneyReservationFromPortfolionCommand confirmCommand =
                new ConfirmMoneyReservationFromPortfolionCommand(getPortfolioIdentifier(), event.getAmountOfItems() * event.getItemPrice());
        getCommandBus().dispatch(confirmCommand);
        AddItemsToPortfolioCommand addItemsCommand =
                new AddItemsToPortfolioCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getAmountOfItems());
        getCommandBus().dispatch(addItemsCommand);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(BuyTransactionPartiallyExecutedEvent event) {
        ConfirmMoneyReservationFromPortfolionCommand confirmCommand =
                new ConfirmMoneyReservationFromPortfolionCommand(getPortfolioIdentifier(), event.getAmountOfExecutedItems() * event.getItemPrice());
        getCommandBus().dispatch(confirmCommand);
        AddItemsToPortfolioCommand addItemsCommand =
                new AddItemsToPortfolioCommand(getPortfolioIdentifier(), getOrderbookIdentifier(), event.getAmountOfExecutedItems());
        getCommandBus().dispatch(addItemsCommand);
    }

}
