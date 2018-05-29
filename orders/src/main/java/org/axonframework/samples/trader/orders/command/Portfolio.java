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

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.transaction.TransactionId;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.cash.*;
import org.axonframework.samples.trader.api.portfolio.stock.*;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Not a lot of checks are available. We will check if you still have item before you reserve them. Other than that
 * we will not do checks. It is possible to give more items than you reserve.
 * <p/>
 * When buying items you need to reserve cash. Reservations need to be confirmed or cancelled. It is up to the user
 * to confirm and cancel the right amounts. The Portfolio does not keep track of it.
 */
@Aggregate(repository = "portfolioAggregateRepository")
public class Portfolio {

    private static final long DEFAULT_ITEM_VALUE = 0L;

    @AggregateIdentifier
    private PortfolioId portfolioId;
    private Map<OrderBookId, Long> availableItems;
    private Map<OrderBookId, Long> reservedItems;
    private long amountOfMoney;

    @SuppressWarnings("UnusedDeclaration")
    public Portfolio() {
        // Required by Axon Framework
    }

    @CommandHandler
    public Portfolio(CreatePortfolioCommand cmd) {
        apply(new PortfolioCreatedEvent(cmd.getPortfolioId(), cmd.getUserId()));
    }

    @CommandHandler
    public void handle(AddItemsToPortfolioCommand cmd) {
        apply(new ItemsAddedToPortfolioEvent(portfolioId, cmd.getOrderBookId(), cmd.getAmountOfItemsToAdd()));
    }

    @CommandHandler
    public void handle(ReserveItemsCommand cmd) {
        OrderBookId orderBookId = cmd.getOrderBookId();
        TransactionId transactionId = cmd.getTransactionId();
        long amountOfItemsToReserve = cmd.getAmountOfItemsToReserve();

        if (!availableItems.containsKey(orderBookId)) {
            apply(new ItemToReserveNotAvailableInPortfolioEvent(portfolioId, orderBookId, transactionId));
        } else {
            Long availableAmountOfItems = availableItems.get(orderBookId);
            if (availableAmountOfItems < amountOfItemsToReserve) {
                apply(new NotEnoughItemsAvailableToReserveInPortfolioEvent(
                        portfolioId,
                        orderBookId,
                        transactionId,
                        availableAmountOfItems,
                        amountOfItemsToReserve));
            } else {
                apply(new ItemsReservedEvent(portfolioId,
                                             orderBookId,
                                             transactionId,
                                             amountOfItemsToReserve));
            }
        }
    }

    @CommandHandler
    public void handle(ConfirmItemReservationForPortfolioCommand cmd) {
        apply(new ItemReservationConfirmedForPortfolioEvent(portfolioId,
                                                            cmd.getOrderBookId(),
                                                            cmd.getTransactionId(),
                                                            cmd.getAmountOfItemsToConfirm()));
    }

    @CommandHandler
    public void handle(CancelItemReservationForPortfolioCommand cmd) {
        apply(new ItemReservationCancelledForPortfolioEvent(
                portfolioId,
                cmd.getOrderBookId(),
                cmd.getTransactionId(),
                cmd.getAmountOfItemsToCancel()));
    }

    @CommandHandler
    public void handle(DepositCashCommand cmd) {
        apply(new CashDepositedEvent(portfolioId, cmd.getMoneyToAddInCents()));
    }

    @CommandHandler
    public void handle(WithdrawCashCommand cmd) {
        apply(new CashWithdrawnEvent(portfolioId, cmd.getAmountToPayInCents()));
    }

    @CommandHandler
    public void handle(ReserveCashCommand cmd) {
        TransactionId transactionId = cmd.getTransactionId();
        long amountToReserve = cmd.getAmountOfMoneyToReserve();

        if (amountOfMoney >= amountToReserve) {
            apply(new CashReservedEvent(portfolioId, transactionId, amountToReserve));
        } else {
            apply(new CashReservationRejectedEvent(portfolioId, transactionId, amountToReserve));
        }
    }

    @CommandHandler
    public void handle(ConfirmCashReservationCommand cmd) {
        apply(new CashReservationConfirmedEvent(portfolioId,
                                                cmd.getTransactionId(),
                                                cmd.getAmountOfMoneyToConfirmInCents()));
    }

    @CommandHandler
    public void handle(CancelCashReservationCommand cmd) {
        apply(new CashReservationCancelledEvent(portfolioId, cmd.getTransactionId(), cmd.getAmountOfMoneyToCancel()));
    }

    @EventSourcingHandler
    public void on(PortfolioCreatedEvent event) {
        portfolioId = event.getPortfolioId();
        availableItems = new HashMap<>();
        reservedItems = new HashMap<>();
    }

    @EventSourcingHandler
    public void on(ItemsAddedToPortfolioEvent event) {
        long available = obtainCurrentAvailableItems(event.getOrderBookId());
        availableItems.put(event.getOrderBookId(), available + event.getAmountOfItemsAdded());
    }

    @EventSourcingHandler
    public void on(ItemsReservedEvent event) {
        long available = obtainCurrentAvailableItems(event.getOrderBookId());
        availableItems.put(event.getOrderBookId(), available - event.getAmountOfItemsReserved());

        long reserved = obtainCurrentReservedItems(event.getOrderBookId());
        reservedItems.put(event.getOrderBookId(), reserved + event.getAmountOfItemsReserved());
    }

    @EventSourcingHandler
    public void on(ItemReservationConfirmedForPortfolioEvent event) {
        long reserved = obtainCurrentReservedItems(event.getOrderBookId());
        reservedItems.put(event.getOrderBookId(), reserved - event.getAmountOfConfirmedItems());
    }

    @EventSourcingHandler
    public void on(ItemReservationCancelledForPortfolioEvent event) {
        long reserved = obtainCurrentReservedItems(event.getOrderBookId());
        reservedItems.put(event.getOrderBookId(), reserved - event.getAmountOfCancelledItems());

        long available = obtainCurrentAvailableItems(event.getOrderBookId());
        availableItems.put(event.getOrderBookId(), available + event.getAmountOfCancelledItems());
    }

    private long obtainCurrentReservedItems(OrderBookId orderBookId) {
        return reservedItems.getOrDefault(orderBookId, DEFAULT_ITEM_VALUE);
    }

    private long obtainCurrentAvailableItems(OrderBookId orderBookId) {
        return availableItems.getOrDefault(orderBookId, DEFAULT_ITEM_VALUE);
    }

    @EventSourcingHandler
    public void on(CashDepositedEvent event) {
        amountOfMoney += event.getMoneyAddedInCents();
    }

    @EventSourcingHandler
    public void on(CashWithdrawnEvent event) {
        amountOfMoney -= event.getAmountPaidInCents();
    }

    @EventSourcingHandler
    public void on(CashReservedEvent event) {
        amountOfMoney -= event.getAmountToReserve();
    }

    @EventSourcingHandler
    public void on(CashReservationCancelledEvent event) {
        amountOfMoney += event.getAmountOfMoneyToCancel();
    }
}
