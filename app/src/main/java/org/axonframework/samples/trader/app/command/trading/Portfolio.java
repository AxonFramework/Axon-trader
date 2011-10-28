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
import org.axonframework.samples.trader.app.api.portfolio.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Not a lot of checks are available. We will check if you still have item before you reserve them. Other than that
 * we will not do checks. It is possible to give more items than you reserve.
 *
 * @author Jettro Coenradie
 */
public class Portfolio extends AbstractAnnotatedAggregateRoot {
    private Map<AggregateIdentifier, Integer> availableItems = new HashMap<AggregateIdentifier, Integer>();
    private Map<AggregateIdentifier, Integer> reservedItems = new HashMap<AggregateIdentifier, Integer>();


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
        apply(new ReservationConfirmedForPortfolioEvent(itemIdentifier, amountOfItemsToConfirm));
    }

    public void cancelReservation(AggregateIdentifier itemIdentifier, int amountOfItemsToCancel) {
        apply(new ReservationCancelledForPortfolioEvent(itemIdentifier, amountOfItemsToCancel));
    }

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
    public void onReservationConfirmed(ReservationConfirmedForPortfolioEvent event) {
        int reserved = obtainCurrentReservedItems(event.getItemIdentifier());
        reservedItems.put(event.getItemIdentifier(), reserved - event.getAmountOfConfirmedItems());
    }

    @EventHandler
    public void onReservationCancelled(ReservationCancelledForPortfolioEvent event) {
        int reserved = obtainCurrentReservedItems(event.getItemIdentifier());
        reservedItems.put(event.getItemIdentifier(), reserved + event.getAmountOfCancelledItems());

        int available = obtainCurrentAvailableItems(event.getItemIdentifier());
        availableItems.put(event.getItemIdentifier(), available + event.getAmountOfCancelledItems());

    }

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
