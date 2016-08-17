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
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.cash.CancelCashReservationCommand;
import org.axonframework.samples.trader.api.portfolio.cash.ConfirmCashReservationCommand;
import org.axonframework.samples.trader.api.portfolio.cash.DepositCashCommand;
import org.axonframework.samples.trader.api.portfolio.cash.ReserveCashCommand;
import org.axonframework.samples.trader.api.portfolio.cash.WithdrawCashCommand;
import org.axonframework.samples.trader.api.portfolio.stock.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.stock.CancelItemReservationForPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.stock.ConfirmItemReservationForPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.stock.ReserveItemsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class PortfolioCommandHandler {

    private Repository<Portfolio> portfolioRepository;

    @CommandHandler
    public void handleCreatePortfolio(CreatePortfolioCommand command) throws Exception {
        portfolioRepository.newInstance(() -> new Portfolio(command.getPortfolioId(), command.getUserId()));
    }

    @CommandHandler
    public void handleReserveItemsCommand(ReserveItemsCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.reserveItems(command.getOrderBookIdentifier(),
                                       command.getTransactionIdentifier(),
                                       command.getAmountOfItemsToReserve());
        });
    }

    @CommandHandler
    public void handleAddItemsToPortfolioCommand(AddItemsToPortfolioCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.addItems(command.getOrderBookIdentifier(), command.getAmountOfItemsToAdd());
        });
    }

    @CommandHandler
    public void handleConfirmReservationCommand(ConfirmItemReservationForPortfolioCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.confirmReservation(command.getOrderBookIdentifier(),
                                             command.getTransactionIdentifier(),
                                             command.getAmountOfItemsToConfirm());
        });
    }

    @CommandHandler
    public void handleCancelReservationCommand(CancelItemReservationForPortfolioCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.cancelReservation(command.getOrderBookIdentifier(),
                                            command.getTransactionIdentifier(),
                                            command.getAmountOfItemsToCancel());
        });
    }

    @CommandHandler
    public void handleAddMoneyToPortfolioCommand(DepositCashCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> aggregateRoot.addMoney(command.getMoneyToAddInCents()));
    }

    @CommandHandler
    public void handleMakePaymentFromPortfolioCommand(WithdrawCashCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> aggregateRoot.makePayment(command.getAmountToPayInCents()));
    }

    @CommandHandler
    public void handleReserveMoneyFromPortfolioCommand(ReserveCashCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.reserveMoney(command.getTransactionIdentifier(), command.getAmountOfMoneyToReserve());
        });
    }

    @CommandHandler
    public void handleCancelMoneyReservationFromPortfolioCommand(CancelCashReservationCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.cancelMoneyReservation(command.getTransactionIdentifier(),
                                                 command.getAmountOfMoneyToCancel());
        });
    }

    @CommandHandler
    public void handleConfirmMoneyReservationFromPortfolioCommand(
            ConfirmCashReservationCommand command) {
        Aggregate<Portfolio> portfolio = portfolioRepository.load(command.getPortfolioIdentifier().toString());
        portfolio.execute(aggregateRoot -> {
            aggregateRoot.confirmMoneyReservation(command.getTransactionIdentifier(),
                                                  command.getAmountOfMoneyToConfirmInCents());
        });
    }

    @Autowired
    @Qualifier("portfolioRepository")
    public void setRepository(Repository<Portfolio> repository) {
        this.portfolioRepository = repository;
    }
}
