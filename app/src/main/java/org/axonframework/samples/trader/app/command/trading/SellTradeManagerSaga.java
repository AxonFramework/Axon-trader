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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.axonframework.samples.trader.app.api.order.CreateSellOrderCommand;
import org.axonframework.samples.trader.app.api.portfolio.item.ItemsReservedEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.NotEnoughItemsAvailableToReserveInPortfolio;
import org.axonframework.samples.trader.app.api.portfolio.item.ReserveItemsCommand;
import org.axonframework.samples.trader.app.api.transaction.ConfirmTransactionCommand;
import org.axonframework.samples.trader.app.api.transaction.SellTransactionConfirmedEvent;
import org.axonframework.samples.trader.app.api.transaction.SellTransactionStartedEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jettro Coenradie
 */
public class SellTradeManagerSaga extends AbstractAnnotatedSaga {

    private transient CommandBus commandBus;
    private long totalItems;
    private long pricePerItem;

    private AggregateIdentifier transactionIdentifier;
    private AggregateIdentifier orderbookIdentifier;
    private AggregateIdentifier portfolioIdentifier;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(SellTransactionStartedEvent event) {
        transactionIdentifier = event.getTransactionIdentifier();
        orderbookIdentifier = event.getOrderbookIdentifier();
        portfolioIdentifier = event.getPortfolioIdentifier();

        associateWith("orderbookIdentifier", orderbookIdentifier);
        associateWith("portfolioIdentifier", portfolioIdentifier);

        ReserveItemsCommand reserveItemsCommand =
                new ReserveItemsCommand(portfolioIdentifier, orderbookIdentifier, event.getTotalItems());
        pricePerItem = event.getPricePerItem(); // TODO jettro: correct the long and ints
        totalItems = event.getTotalItems();
        commandBus.dispatch(reserveItemsCommand);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    public void handle(ItemsReservedEvent event) {
        ConfirmTransactionCommand confirmTransactionCommand = new ConfirmTransactionCommand(transactionIdentifier);
        commandBus.dispatch(confirmTransactionCommand);
    }

    @SagaEventHandler(associationProperty = "transactionIdentifier")
    public void handle(SellTransactionConfirmedEvent event) {
        CreateSellOrderCommand command = new CreateSellOrderCommand(portfolioIdentifier, orderbookIdentifier, totalItems, pricePerItem);
        commandBus.dispatch(command);
    }

    @SagaEventHandler(associationProperty = "portfolioIdentifier")
    public void handle(NotEnoughItemsAvailableToReserveInPortfolio event) {
        // TODO jettro: not sure if I need to do something here, maybe inactivate this saga ?
    }

    /*-------------------------------------------------------------------------------------------*/
    /* Getters and setters                                                                       */
    /*-------------------------------------------------------------------------------------------*/
    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }
}
