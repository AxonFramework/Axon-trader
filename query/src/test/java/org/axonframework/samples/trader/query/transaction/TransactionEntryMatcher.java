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

package org.axonframework.samples.trader.query.transaction;

import org.axonframework.samples.trader.orders.api.transaction.TransactionType;
import org.hamcrest.Description;
import org.mockito.*;

/**
 * @author Jettro Coenradie
 */
public class TransactionEntryMatcher extends ArgumentMatcher<TransactionEntry> {

    private String problem;

    private TransactionState state;
    private TransactionType type;
    private String companyName;
    private int amountOfItems;
    private int amountOfItemsExecuted;
    private long pricePerItem;

    public TransactionEntryMatcher(int amountOfItems, int amountOfItemsExecuted, String companyName, long pricePerItem,
                                   TransactionState state, TransactionType type) {
        this.amountOfItems = amountOfItems;
        this.amountOfItemsExecuted = amountOfItemsExecuted;
        this.companyName = companyName;
        this.pricePerItem = pricePerItem;
        this.state = state;
        this.type = type;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof TransactionEntry)) {
            problem = String.format("Wrong argument type, required %s but received %s",
                                    TransactionEntry.class.getName(),
                                    argument.getClass().getName());
            return false;
        }
        TransactionEntry transactionEntry = (TransactionEntry) argument;
        if (amountOfItems != transactionEntry.getAmountOfItems()) {
            problem = String.format("Amount of items is not %d but %d",
                                    amountOfItems,
                                    transactionEntry.getAmountOfItems());
            return false;
        }
        if (amountOfItemsExecuted != transactionEntry.getAmountOfExecutedItems()) {
            problem = String.format("Amount of executed items is not %d but %d",
                                    amountOfItemsExecuted,
                                    transactionEntry.getAmountOfExecutedItems());
            return false;
        }
        if (!companyName.equals(transactionEntry.getCompanyName())) {
            problem = String.format("Company name is not %s but %s", companyName, transactionEntry.getCompanyName());
            return false;
        }
        if (pricePerItem != transactionEntry.getPricePerItem()) {
            problem = String.format("Price per item is not %d but %d",
                                    pricePerItem,
                                    transactionEntry.getPricePerItem());
            return false;
        }
        if (state != transactionEntry.getState()) {
            problem = String.format("State is not %s but %s", state, transactionEntry.getState());
            return false;
        }
        if (type != transactionEntry.getType()) {
            problem = String.format("Type is not %s but %s", type, transactionEntry.getType());
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(problem);
    }
}
