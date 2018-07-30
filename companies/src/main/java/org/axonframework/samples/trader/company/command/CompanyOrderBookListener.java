/*
 * Copyright (c) 2012. Axon Framework
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

package org.axonframework.samples.trader.company.command;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.company.AddOrderBookToCompanyCommand;
import org.axonframework.samples.trader.api.company.CompanyCreatedEvent;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.orders.trades.CreateOrderBookCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This listener is used to create order book instances when we have created a new company</p>
 */
@Service
@ProcessingGroup("commandPublishingEventHandlers")
public class CompanyOrderBookListener {

    private static final Logger logger = LoggerFactory.getLogger(CompanyOrderBookListener.class);

    private final CommandGateway commandGateway;

    @Autowired
    public CompanyOrderBookListener(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void on(CompanyCreatedEvent event) {
        logger.debug("About to dispatch a new command to create an OrderBook for the company {}", event.getCompanyId());

        OrderBookId orderBookId = new OrderBookId();
        commandGateway.send(new CreateOrderBookCommand(orderBookId));
        commandGateway.send(new AddOrderBookToCompanyCommand(event.getCompanyId(), orderBookId));
    }
}
