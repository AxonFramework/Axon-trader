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

import org.axonframework.samples.trader.api.orders.TransactionType;
import org.hamcrest.Description;
import org.mockito.*;

public class TransactionEntryMatcher extends ArgumentMatcher<TransactionView> {

    private final TransactionState state;
    private final TransactionType type;
    private final String companyName;
    private final int amountOfItems;
    private final int amountOfItemsExecuted;
    private final long pricePerItem;

    private String problemDescription;

    public TransactionEntryMatcher(int amountOfItems,
                                   int amountOfItemsExecuted,
                                   String companyName,
                                   long pricePerItem,
                                   TransactionState state,
                                   TransactionType type) {
        this.amountOfItems = amountOfItems;
        this.amountOfItemsExecuted = amountOfItemsExecuted;
        this.companyName = companyName;
        this.pricePerItem = pricePerItem;
        this.state = state;
        this.type = type;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof TransactionView)) {
            problemDescription = String.format("Wrong argument type, required %s but received %s",
                                               TransactionView.class.getName(),
                                               argument.getClass().getName());
            return false;
        }
        TransactionView transactionView = (TransactionView) argument;
        if (amountOfItems != transactionView.getAmountOfItems()) {
            problemDescription = String.format("Amount of items is not %d but %d",
                                               amountOfItems,
                                               transactionView.getAmountOfItems());
            return false;
        }
        if (amountOfItemsExecuted != transactionView.getAmountOfExecutedItems()) {
            problemDescription = String.format("Amount of executed items is not %d but %d",
                                               amountOfItemsExecuted,
                                               transactionView.getAmountOfExecutedItems());
            return false;
        }
        if (!companyName.equals(transactionView.getCompanyName())) {
            problemDescription = String.format("Company name is not %s but %s",
                                               companyName,
                                               transactionView.getCompanyName());
            return false;
        }
        if (pricePerItem != transactionView.getPricePerItem()) {
            problemDescription = String.format("Price per item is not %d but %d",
                                               pricePerItem,
                                               transactionView.getPricePerItem());
            return false;
        }
        if (state != transactionView.getState()) {
            problemDescription = String.format("State is not %s but %s", state, transactionView.getState());
            return false;
        }
        if (type != transactionView.getType()) {
            problemDescription = String.format("Type is not %s but %s", type, transactionView.getType());
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(problemDescription);
    }
}
