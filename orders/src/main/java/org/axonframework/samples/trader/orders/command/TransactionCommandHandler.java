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
import org.axonframework.samples.trader.api.orders.transaction.CancelTransactionCommand;
import org.axonframework.samples.trader.api.orders.transaction.ConfirmTransactionCommand;
import org.axonframework.samples.trader.api.orders.transaction.ExecutedTransactionCommand;
import org.axonframework.samples.trader.api.orders.transaction.StartBuyTransactionCommand;
import org.axonframework.samples.trader.api.orders.transaction.StartSellTransactionCommand;
import org.axonframework.samples.trader.api.orders.transaction.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class TransactionCommandHandler {

    private Repository<Transaction> repository;

    @CommandHandler
    public void handleStartBuyTransactionCommand(StartBuyTransactionCommand command) throws Exception {
        repository.newInstance(() -> {
            return new Transaction(
                    command.getTransactionIdentifier(),
                    TransactionType.BUY,
                    command.getOrderbookIdentifier(),
                    command.getPortfolioIdentifier(),
                    command.getTradeCount(),
                    command.getItemPrice());
        });
    }

    @CommandHandler
    public void handleStartSellTransactionCommand(StartSellTransactionCommand command) throws Exception {
        repository.newInstance(() -> {
            return new Transaction(
                    command.getTransactionIdentifier(),
                    TransactionType.SELL,
                    command.getOrderbookIdentifier(),
                    command.getPortfolioIdentifier(),
                    command.getTradeCount(),
                    command.getItemPrice());
        });
    }

    @CommandHandler
    public void handleConfirmTransactionCommand(ConfirmTransactionCommand command) {
        Aggregate<Transaction> transaction = repository.load(command.getTransactionIdentifier().toString());
        transaction.execute(aggregateRoot -> aggregateRoot.confirm());
    }

    @CommandHandler
    public void handleCancelTransactionCommand(CancelTransactionCommand command) {
        Aggregate<Transaction> transaction = repository.load(command.getTransactionIdentifier().toString());
        transaction.execute(aggregateRoot -> aggregateRoot.cancel());
    }

    @CommandHandler
    public void handleExecutedTransactionCommand(ExecutedTransactionCommand command) {
        Aggregate<Transaction> transaction = repository.load(command.getTransactionIdentifier().toString());
        transaction.execute(aggregateRoot -> aggregateRoot.execute(command.getAmountOfItems(), command.getItemPrice()));
    }

    @Autowired
    @Qualifier("transactionRepository")
    public void setRepository(Repository<Transaction> repository) {
        this.repository = repository;
    }
}
