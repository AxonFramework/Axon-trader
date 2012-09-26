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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.api.company.AddOrderBookToCompanyCommand;
import org.axonframework.samples.trader.api.company.CompanyCreatedEvent;
import org.axonframework.samples.trader.api.orders.trades.CreateOrderBookCommand;
import org.axonframework.samples.trader.api.orders.trades.OrderBookId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>This listener is used to create orderbook instances when we have created a new company</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class CompanyOrderBookListener {
    private final static Logger logger = LoggerFactory.getLogger(CompanyOrderBookListener.class);
    private CommandBus commandBus;

    @EventHandler
    public void handleCompanyCreated(CompanyCreatedEvent event) {
        logger.debug("About to dispatch a new command to create an OrderBook for the company {}",
                event.getCompanyIdentifier());

        OrderBookId orderBookId = new OrderBookId();
        CreateOrderBookCommand createOrderBookCommand = new CreateOrderBookCommand(orderBookId);
        commandBus.dispatch(new GenericCommandMessage<CreateOrderBookCommand>(createOrderBookCommand));

        AddOrderBookToCompanyCommand addOrderBookToCompanyCommand =
                new AddOrderBookToCompanyCommand(event.getCompanyIdentifier(), orderBookId);
        commandBus.dispatch(new GenericCommandMessage<AddOrderBookToCompanyCommand>(addOrderBookToCompanyCommand));
    }

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }
}
