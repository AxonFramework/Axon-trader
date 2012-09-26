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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Listener that is used to create a new portfolio for each new user that is created.</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class PortfolioManagementUserListener {

    private final static Logger logger = LoggerFactory.getLogger(PortfolioManagementUserListener.class);
    private CommandBus commandBus;

    @EventHandler
    public void createNewPortfolioWhenUserIsCreated(UserCreatedEvent event) {
        logger.debug("About to dispatch a new command to create a Portfolio for the new user {}",
                event.getUserIdentifier());
        CreatePortfolioCommand command = new CreatePortfolioCommand(new PortfolioId(), event.getUserIdentifier());
        commandBus.dispatch(new GenericCommandMessage<CreatePortfolioCommand>(command));
    }

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }
}
