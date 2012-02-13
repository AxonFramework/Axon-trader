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

package org.axonframework.samples.trader.orders.command.trading;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.orders.api.transaction.*;
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
    public void handleStartBuyTransactionCommand(StartBuyTransactionCommand command) {
        Transaction transaction = new Transaction(
                TransactionType.BUY,
                command.getOrderbookIdentifier(),
                command.getPortfolioIdentifier(),
                command.getTradeCount(),
                command.getItemPrice());
        repository.add(transaction);
    }

    @CommandHandler
    public void handleStartSellTransactionCommand(StartSellTransactionCommand command) {
        Transaction transaction = new Transaction(
                TransactionType.SELL,
                command.getOrderbookIdentifier(),
                command.getPortfolioIdentifier(),
                command.getTradeCount(),
                command.getItemPrice());
        repository.add(transaction);
    }

    @CommandHandler
    public void handleConfirmTransactionCommand(ConfirmTransactionCommand command) {
        Transaction transaction = repository.load(command.getTransactionIdentifier());
        transaction.confirm();
    }

    @CommandHandler
    public void handleCancelTransactionCommand(CancelTransactionCommand command) {
        Transaction transaction = repository.load(command.getTransactionIdentifier());
        transaction.cancel();
    }

    @CommandHandler
    public void handleExecutedTransactionCommand(ExecutedTransactionCommand command) {
        Transaction transaction = repository.load(command.getTransactionIdentifier());
        transaction.execute(command.getAmountOfItems(), command.getItemPrice());
    }

    @Autowired
    @Qualifier("transactionRepository")
    public void setRepository(Repository<Transaction> repository) {
        this.repository = repository;
    }
}
