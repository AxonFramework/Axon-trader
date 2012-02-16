/*
 * Copyright (c) 2012. Gridshore
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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.orders.api.transaction.ExecutedTransactionCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class ExecutedTransactionCommandMatcher extends BaseMatcher<ExecutedTransactionCommand> {
    private String transactionIdentifier;
    private long amountOfItems;
    private long itemPrice;

    public ExecutedTransactionCommandMatcher(long amountOfItems, long itemPrice, AggregateIdentifier transactionIdentifier) {
        this.amountOfItems = amountOfItems;
        this.itemPrice = itemPrice;
        this.transactionIdentifier = transactionIdentifier.asString();
    }

    @Override
    public boolean matches(Object object) {
        if (!(object instanceof ExecutedTransactionCommand)) {
            return false;
        }
        ExecutedTransactionCommand command = (ExecutedTransactionCommand) object;
        return command.getTransactionIdentifier().asString().equals(transactionIdentifier)
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
