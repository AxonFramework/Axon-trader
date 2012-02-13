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

package org.axonframework.samples.trader.orders.command.trading;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.orders.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.orders.api.portfolio.item.*;
import org.axonframework.samples.trader.orders.api.portfolio.money.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Not a lot of checks are available. We will check if you still have item before you reserve them. Other than that
 * we will not do checks. It is possible to give more items than you reserve.
 * <p/>
 * When buying items you need to reserve money. Reservations need to be confirmed or cancelled. It is up to the user
 * to confirm and cancel the right amounts. The Portfolio does not keep track of it.
 *
 * @author Jettro Coenradie
 */
public class Portfolio extends AbstractAnnotatedAggregateRoot {
    private Map<AggregateIdentifier, Long> availableItems = new HashMap<AggregateIdentifier, Long>();
    private Map<AggregateIdentifier, Long> reservedItems = new HashMap<AggregateIdentifier, Long>();

    private long amountOfMoney;
    private long reservedAmountOfMoney;

    public Portfolio(AggregateIdentifier identifier) {
        super(identifier);
    }

    public Portfolio(AggregateIdentifier aggregateIdentifier, AggregateIdentifier userIdentifier) {
        super(aggregateIdentifier);
        apply(new PortfolioCreatedEvent(userIdentifier));
    }

    public void addItems(AggregateIdentifier orderBookIdentifier, long amountOfItemsToAdd) {
        apply(new ItemsAddedToPortfolioEvent(orderBookIdentifier, amountOfItemsToAdd));
    }

    public void reserveItems(AggregateIdentifier orderBookIdentifier, AggregateIdentifier transactionIdentifier, long amountOfItemsToReserve) {
        if (!availableItems.containsKey(orderBookIdentifier)) {
            apply(new ItemToReserveNotAvailableInPortfolioEvent(orderBookIdentifier, transactionIdentifier));
        } else {
            Long availableAmountOfItems = availableItems.get(orderBookIdentifier);
            if (availableAmountOfItems < amountOfItemsToReserve) {
                apply(new NotEnoughItemsAvailableToReserveInPortfolio(
                        orderBookIdentifier, transactionIdentifier, availableAmountOfItems, amountOfItemsToReserve));
            } else {
                apply(new ItemsReservedEvent(orderBookIdentifier, transactionIdentifier, amountOfItemsToReserve));
            }
        }
    }

    public void confirmReservation(AggregateIdentifier orderBookIdentifier, AggregateIdentifier transactionIdentifier, long amountOfItemsToConfirm) {
        apply(new ItemReservationConfirmedForPortfolioEvent(orderBookIdentifier, transactionIdentifier, amountOfItemsToConfirm));
    }

    public void cancelReservation(AggregateIdentifier orderBookIdentifier, AggregateIdentifier transactionIdentifier, long amountOfItemsToCancel) {
        apply(new ItemReservationCancelledForPortfolioEvent(orderBookIdentifier, transactionIdentifier, amountOfItemsToCancel));
    }

    public void addMoney(long moneyToAddInCents) {
        apply(new MoneyDepositedToPortfolioEvent(moneyToAddInCents));
    }

    public void makePayment(long amountToPayInCents) {
        apply(new MoneyWithdrawnFromPortfolioEvent(amountToPayInCents));
    }

    public void reserveMoney(AggregateIdentifier transactionIdentifier, long amountToReserve) {
        if (amountOfMoney >= amountToReserve) {
            apply(new MoneyReservedFromPortfolioEvent(transactionIdentifier, amountToReserve));
        } else {
            apply(new NotEnoughMoneyInPortfolioToMakeReservationEvent(transactionIdentifier, amountToReserve));
        }
    }

    public void cancelMoneyReservation(AggregateIdentifier transactionIdentifier, long amountOfMoneyToCancel) {
        apply(new MoneyReservationCancelledFromPortfolioEvent(transactionIdentifier, amountOfMoneyToCancel));
    }

    public void confirmMoneyReservation(AggregateIdentifier transactionIdentifier, long amountOfMoneyToConfirm) {
        apply(new MoneyReservationConfirmedFromPortfolioEvent(transactionIdentifier, amountOfMoneyToConfirm));
    }

    /* EVENT HANDLING */
    @EventHandler
    public void onPortfolioCreated(PortfolioCreatedEvent event) {
        // nothing for now
    }

    @EventHandler
    public void onNoItemsAvailableToReserve(ItemToReserveNotAvailableInPortfolioEvent event) {
        // nothing for now
    }

    @EventHandler
    public void onNotEnoughItemsAvailableToReserve(NotEnoughItemsAvailableToReserveInPortfolio event) {
        // nothing for now
    }

    @EventHandler
    public void onItemsAddedToPortfolio(ItemsAddedToPortfolioEvent event) {
        long available = obtainCurrentAvailableItems(event.getOrderBookIdentifier());
        availableItems.put(event.getOrderBookIdentifier(), available + event.getAmountOfItemsAdded());
    }

    @EventHandler
    public void onItemsReserved(ItemsReservedEvent event) {
        long available = obtainCurrentAvailableItems(event.getOrderBookIdentifier());
        availableItems.put(event.getOrderBookIdentifier(), available - event.getAmountOfItemsReserved());

        long reserved = obtainCurrentReservedItems(event.getOrderBookIdentifier());
        reservedItems.put(event.getOrderBookIdentifier(), reserved + event.getAmountOfItemsReserved());
    }

    @EventHandler
    public void onReservationConfirmed(ItemReservationConfirmedForPortfolioEvent event) {
        long reserved = obtainCurrentReservedItems(event.getOrderBookIdentifier());
        reservedItems.put(event.getOrderBookIdentifier(), reserved - event.getAmountOfConfirmedItems());

        long available = obtainCurrentAvailableItems(event.getOrderBookIdentifier());
        availableItems.put(event.getOrderBookIdentifier(), available - event.getAmountOfConfirmedItems());

    }

    @EventHandler
    public void onReservationCancelled(ItemReservationCancelledForPortfolioEvent event) {
        long reserved = obtainCurrentReservedItems(event.getOrderBookIdentifier());
        reservedItems.put(event.getOrderBookIdentifier(), reserved + event.getAmountOfCancelledItems());

        long available = obtainCurrentAvailableItems(event.getOrderBookIdentifier());
        availableItems.put(event.getOrderBookIdentifier(), available + event.getAmountOfCancelledItems());
    }

    @EventHandler
    public void onMoneyAddedToPortfolio(MoneyDepositedToPortfolioEvent event) {
        amountOfMoney += event.getMoneyAddedInCents();
    }

    @EventHandler
    public void onPaymentMadeFromPortfolio(MoneyWithdrawnFromPortfolioEvent event) {
        amountOfMoney -= event.getAmountPaidInCents();
    }

    @EventHandler
    public void onMoneyReservedFromPortfolio(MoneyReservedFromPortfolioEvent event) {
        amountOfMoney -= event.getAmountToReserve();
        reservedAmountOfMoney += event.getAmountToReserve();
    }

    @EventHandler
    public void onNotEnoughMoneyToMakeReservation(NotEnoughMoneyInPortfolioToMakeReservationEvent event) {
        // do nothing
    }

    @EventHandler
    public void onMoneyReservationCancelled(MoneyReservationCancelledFromPortfolioEvent event) {
        amountOfMoney += event.getAmountOfMoneyToCancel();
        reservedAmountOfMoney -= event.getAmountOfMoneyToCancel();
    }

    @EventHandler
    public void onMoneyReservationConfirmed(MoneyReservationConfirmedFromPortfolioEvent event) {
        reservedAmountOfMoney -= event.getAmountOfMoneyConfirmedInCents();
    }

    /* UTILITY METHODS */
    private long obtainCurrentAvailableItems(AggregateIdentifier itemIdentifier) {
        long available = 0;
        if (availableItems.containsKey(itemIdentifier)) {
            available = availableItems.get(itemIdentifier);
        }
        return available;
    }

    private long obtainCurrentReservedItems(AggregateIdentifier itemIdentifier) {
        long reserved = 0;
        if (reservedItems.containsKey(itemIdentifier)) {
            reserved = reservedItems.get(itemIdentifier);
        }
        return reserved;
    }
}
