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

package org.axonframework.samples.trader.app.query.portfolio;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.portfolio.PortfolioCreatedEvent;
import org.axonframework.samples.trader.app.api.portfolio.money.MoneyDepositedToPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.money.MoneyReservedFromPortfolioEvent;
import org.axonframework.samples.trader.app.api.portfolio.money.MoneyWithdrawnFromPortfolioEvent;
import org.axonframework.samples.trader.app.query.portfolio.repositories.PortfolioQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class PortfolioListener {
    private final static Logger logger = LoggerFactory.getLogger(PortfolioListener.class);

    private PortfolioQueryRepository portfolioRepository;

    @EventHandler
    public void handleEvent(PortfolioCreatedEvent event) {
        logger.debug("About to handle the PortfolioCreatedEvent for user with identifier {}",
                event.getUserIdentifier().asString());

        PortfolioEntry portfolioEntry = new PortfolioEntry();
        portfolioEntry.setIdentifier(event.getPortfolioIdentifier().asString());
        portfolioEntry.setUserIdentifier(event.getUserIdentifier().asString());
        portfolioEntry.setAmountOfMoney(0);
        portfolioEntry.setReservedAmountOfMoney(0);

        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyDepositedToPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() + event.getMoneyAddedInCents());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyWithdrawnFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() - event.getAmountPaidInCents());
        portfolioRepository.save(portfolioEntry);
    }

    @EventHandler
    public void handleEvent(MoneyReservedFromPortfolioEvent event) {
        PortfolioEntry portfolioEntry = portfolioRepository.findOne(event.getPortfolioIdentifier().asString());
        portfolioEntry.setReservedAmountOfMoney(portfolioEntry.getReservedAmountOfMoney() + event.getAmountToReserve());
        portfolioEntry.setAmountOfMoney(portfolioEntry.getAmountOfMoney() - event.getAmountToReserve());
        portfolioRepository.save(portfolioEntry);
    }


    @Autowired
    public void setPortfolioRepository(PortfolioQueryRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }
}
