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

package org.axonframework.samples.trader.orders.command.matchers;

import org.axonframework.samples.trader.orders.api.transaction.ExecutedTransactionCommand;
import org.axonframework.samples.trader.tradeengine.api.order.TransactionId;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class ExecutedTransactionCommandMatcher extends BaseMatcher<ExecutedTransactionCommand> {

    private TransactionId transactionIdentifier;
    private long amountOfItems;
    private long itemPrice;

    public ExecutedTransactionCommandMatcher(long amountOfItems, long itemPrice, TransactionId transactionIdentifier) {
        this.amountOfItems = amountOfItems;
        this.itemPrice = itemPrice;
        this.transactionIdentifier = transactionIdentifier;
    }

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof ExecutedTransactionCommand)) {
            return false;
        }
        ExecutedTransactionCommand command = (ExecutedTransactionCommand) object;
        return command.getTransactionIdentifier().equals(transactionIdentifier)
                && command.getAmountOfItems() == amountOfItems
                && command.getItemPrice() == itemPrice;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ExecutedTransactionCommand with amountOfItems [")
                .appendValue(amountOfItems)
                .appendText("], itemPrice [")
                .appendValue(itemPrice)
                .appendText("] for Transaction with identifier [")
                .appendValue(transactionIdentifier)
                .appendText("]");
    }
}
