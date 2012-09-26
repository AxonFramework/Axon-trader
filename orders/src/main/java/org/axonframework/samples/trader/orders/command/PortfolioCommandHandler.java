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

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.item.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.item.CancelItemReservationForPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.item.ConfirmItemReservationForPortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.item.ReserveItemsCommand;
import org.axonframework.samples.trader.api.portfolio.money.*;
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
    public void handleCreatePortfolio(CreatePortfolioCommand command) {
        Portfolio portfolio = new Portfolio(command.getPortfolioId(), command.getUserId());
        portfolioRepository.add(portfolio);
    }

    @CommandHandler
    public void handleReserveItemsCommand(ReserveItemsCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.reserveItems(command.getOrderBookIdentifier(),
                command.getTransactionIdentifier(),
                command.getAmountOfItemsToReserve());
    }

    @CommandHandler
    public void handleAddItemsToPortfolioCommand(AddItemsToPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.addItems(command.getOrderBookIdentifier(), command.getAmountOfItemsToAdd());
    }

    @CommandHandler
    public void handleConfirmReservationCommand(ConfirmItemReservationForPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.confirmReservation(command.getOrderBookIdentifier(),
                command.getTransactionIdentifier(),
                command.getAmountOfItemsToConfirm());
    }

    @CommandHandler
    public void handleCancelReservationCommand(CancelItemReservationForPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.cancelReservation(command.getOrderBookIdentifier(),
                command.getTransactionIdentifier(),
                command.getAmountOfItemsToCancel());
    }

    @CommandHandler
    public void handleAddMoneyToPortfolioCommand(DepositMoneyToPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.addMoney(command.getMoneyToAddInCents());
    }

    @CommandHandler
    public void handleMakePaymentFromPortfolioCommand(WithdrawMoneyFromPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.makePayment(command.getAmountToPayInCents());
    }

    @CommandHandler
    public void handleReserveMoneyFromPortfolioCommand(ReserveMoneyFromPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.reserveMoney(command.getTransactionIdentifier(), command.getAmountOfMoneyToReserve());
    }

    @CommandHandler
    public void handleCancelMoneyReservationFromPortfolioCommand(CancelMoneyReservationFromPortfolioCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.cancelMoneyReservation(command.getTransactionIdentifier(), command.getAmountOfMoneyToCancel());
    }

    @CommandHandler
    public void handleConfirmMoneyReservationFromPortfolioCommand(
            ConfirmMoneyReservationFromPortfolionCommand command) {
        Portfolio portfolio = portfolioRepository.load(command.getPortfolioIdentifier());
        portfolio.confirmMoneyReservation(command.getTransactionIdentifier(),
                command.getAmountOfMoneyToConfirmInCents());
    }

    @Autowired
    @Qualifier("portfolioRepository")
    public void setRepository(EventSourcingRepository<Portfolio> repository) {
        this.portfolioRepository = repository;
    }
}
