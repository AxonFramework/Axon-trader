/*
 * Copyright (c) 2010. Gridshore
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

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class TradeItemCommandHandler {
    private Repository<TradeItem> repository;

    @CommandHandler
    public void handleCreateTradeItem(CreateTradeItemCommand command) {
        TradeItem tradeItem = new TradeItem(new UUIDAggregateIdentifier(),
                command.getTradeItemName(),
                command.getTradeItemValue(),
                command.getAmountOfShares());
        repository.add(tradeItem);

    }

    @Autowired
    @Qualifier("tradeItemRepository")
    public void setRepository(Repository<TradeItem> tradeItemRepository) {
        this.repository = tradeItemRepository;
    }

}
