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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.app.api.portfolio.item.*;
import org.axonframework.samples.trader.app.api.portfolio.money.*;

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
    private Map<AggregateIdentifier, Integer> availableItems = new HashMap<AggregateIdentifier, Integer>();
    private Map<AggregateIdentifier, Integer> reservedItems = new HashMap<AggregateIdentifier, Integer>();

    private long amountOfMoney;
    private long reservedAmountOfMoney;

    public Portfolio(AggregateIdentifier identifier) {
        super(identifier);
    }

    public Portfolio(AggregateIdentifier aggregateIdentifier, AggregateIdentifier userIdentifier) {
        super(aggregateIdentifier);
        apply(new PortfolioCreatedEvent(userIdentifier));
    }

    public void addItems(AggregateIdentifier itemIdentifier, int amountOfItemsToAdd) {
        apply(new ItemsAddedToPortfolioEvent(itemIdentifier, amountOfItemsToAdd));
    }

    public void reserveItems(AggregateIdentifier itemIdentifier, int amountOfItemsToReserve) {
        if (!availableItems.containsKey(itemIdentifier)) {
            apply(new ItemToReserveNotAvailableInPortfolioEvent(itemIdentifier));
        } else {
            Integer availableAmountOfItems = availableItems.get(itemIdentifier);
            if (availableAmountOfItems < amountOfItemsToReserve) {
                apply(new NotEnoughItemsAvailableToReserveInPortfolio(
                        itemIdentifier, availableAmountOfItems, amountOfItemsToReserve));
            } else {
                apply(new ItemsReservedEvent(itemIdentifier, amountOfItemsToReserve));
            }
        }
    }

    public void confirmReservation(AggregateIdentifier itemIdentifier, int amountOfItemsToConfirm) {
        apply(new ItemReservationConfirmedForPortfolioEvent(itemIdentifier, amountOfItemsToConfirm));
    }

    public void cancelReservation(AggregateIdentifier itemIdentifier, int amountOfItemsToCancel) {
        apply(new ItemReservationCancelledForPortfolioEvent(itemIdentifier, amountOfItemsToCancel));
    }

    public void addMoney(long moneyToAddInCents) {
        apply(new MoneyAddedToPortfolioEvent(moneyToAddInCents));
    }

    public void makePayment(long amountToPayInCents) {
        if (amountOfMoney >= amountToPayInCents) {
            apply(new PaymentMadeFromPortfolioEvent(amountToPayInCents));
        } else {
            apply(new NotEnoughMoneyInPortfolioToMakeReservationEvent(amountToPayInCents));
        }
    }

    public void reserveMoney(long amountToReserve) {
        if (amountOfMoney >= amountToReserve) {
            apply(new MoneyReservedFromPortfolioEvent(amountToReserve));
        } else {
            apply(new NotEnoughMoneyInPortfolioToMakeReservationEvent(amountToReserve));
        }
    }

    public void cancelMoneyReservation(long amountOfMoneyToCancel) {
        apply(new MoneyReservationCancelledFromPortfolioEvent(amountOfMoneyToCancel));
    }

    public void confirmMoneyReservation(long amountOfMoneyToConfirm) {
        apply(new MoneyReservationConfirmedFromPortfolioEvent(amountOfMoneyToConfirm));
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
        int available = obtainCurrentAvailableItems(event.getItemIdentifier());
        availableItems.put(event.getItemIdentifier(), available + event.getAmountOfItemsAdded());
    }

    @EventHandler
    public void onItemsReserved(ItemsReservedEvent event) {
        int available = obtainCurrentAvailableItems(event.getItemIdentifier());
        availableItems.put(event.getItemIdentifier(), available - event.getAmountOfItemsReserved());

        int reserved = obtainCurrentReservedItems(event.getItemIdentifier());
        reservedItems.put(event.getItemIdentifier(), reserved + event.getAmountOfItemsReserved());
    }

    @EventHandler
    public void onReservationConfirmed(ItemReservationConfirmedForPortfolioEvent event) {
        int reserved = obtainCurrentReservedItems(event.getItemIdentifier());
        reservedItems.put(event.getItemIdentifier(), reserved - event.getAmountOfConfirmedItems());
    }

    @EventHandler
    public void onReservationCancelled(ItemReservationCancelledForPortfolioEvent event) {
        int reserved = obtainCurrentReservedItems(event.getItemIdentifier());
        reservedItems.put(event.getItemIdentifier(), reserved + event.getAmountOfCancelledItems());

        int available = obtainCurrentAvailableItems(event.getItemIdentifier());
        availableItems.put(event.getItemIdentifier(), available + event.getAmountOfCancelledItems());
    }

    @EventHandler
    public void onMoneyAddedToPortfolio(MoneyAddedToPortfolioEvent event) {
        amountOfMoney += event.getMoneyAddedInCents();
    }

    @EventHandler
    public void onPaymentMadeFromPortfolio(PaymentMadeFromPortfolioEvent event) {
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
    private int obtainCurrentAvailableItems(AggregateIdentifier itemIdentifier) {
        int available = 0;
        if (availableItems.containsKey(itemIdentifier)) {
            available = availableItems.get(itemIdentifier);
        }
        return available;
    }

    private int obtainCurrentReservedItems(AggregateIdentifier itemIdentifier) {
        int reserved = 0;
        if (reservedItems.containsKey(itemIdentifier)) {
            reserved = reservedItems.get(itemIdentifier);
        }
        return reserved;
    }
}
