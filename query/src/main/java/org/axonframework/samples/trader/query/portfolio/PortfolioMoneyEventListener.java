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

package org.axonframework.samples.trader.query.portfolio;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.orders.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.orders.api.portfolio.money.*;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class PortfolioMoneyEventListener {

    private final static Logger logger = LoggerFactory.getLogger(PortfolioMoneyEventListener.class);

    private PortfolioQueryRepository portfolioRepository;
    private UserQueryRepository userQueryRepository;

    @EventHandler
    public void handleEvent(PortfolioCreatedEvent event) {
        logger.debug("About to handle the PortfolioCreatedEvent for user with identifier {}",
                event.getUserId().toString());

        PortfolioEntry portfolioEntry = new PortfolioEntry();
        portfolioEntry.setIdentifier(event.getPortfolioId().toString());
        portfolioEntry.setUserIdentifier(event.getUserId().toString());
        portfolioEntry.setUserName(userQueryRepository.findByIdentifier(event.getUserId().toString())
                .getFullName());
        portfolioEntry.setAmountOfMoney(0);
        portfolioEntry.setReservedAmountOfMoney(0);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyDepositedToPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().toString());
        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() + event.getMoneyAddedInCents());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyWithdrawnFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().toString());
        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() - event.getAmountPaidInCents());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyReservedFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().toString());
        portfolioEntry.setReservedAmountOfMoney(portfolioEntry.getReservedAmountOfMoney() + event.getAmountToReserve());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyReservationCancelledFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().toString());
        portfolioEntry.setReservedAmountOfMoney(
                portfolioEntry.getReservedAmountOfMoney() - event.getAmountOfMoneyToCancel());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyReservationConfirmedFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().toString());
        long reservedAmountOfMoney = portfolioEntry.getReservedAmountOfMoney();
        long amountOfMoneyConfirmed = event.getAmountOfMoneyConfirmedInCents();
        if (amountOfMoneyConfirmed < reservedAmountOfMoney) {
            portfolioEntry.setReservedAmountOfMoney(reservedAmountOfMoney - amountOfMoneyConfirmed);
        } else {
            portfolioEntry.setReservedAmountOfMoney(0);
        }

        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() - amountOfMoneyConfirmed);
        portfolioRepository.save(portfolioEntry);
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setPortfolioRepository(PortfolioQueryRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setUserQueryRepository(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }
}
