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

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Listener that is used to create a new portfolio for each new user that is created.
 * TODO #28 might benefit from a cleaner approach still. Think about this
 */
@Service
public class PortfolioManagementUserListener {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioManagementUserListener.class);

    private final CommandGateway commandGateway;

    public PortfolioManagementUserListener(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void on(UserCreatedEvent event) {
        logger.debug("About to dispatch a new command to create a Portfolio for the new user {}", event.getUserId());
        commandGateway.send(new CreatePortfolioCommand(new PortfolioId(), event.getUserId()));
    }
}
