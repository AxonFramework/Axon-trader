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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.orders.api.transaction.CancelTransactionCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class CancelTransactionCommandMatcher extends BaseMatcher<CancelTransactionCommand> {

    private String transactionIdentifier;

    public CancelTransactionCommandMatcher(AggregateIdentifier transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier.asString();
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof CancelTransactionCommand)) {
            return false;
        }
        CancelTransactionCommand command = (CancelTransactionCommand) o;
        return command.getTransactionIdentifier().asString().equals(transactionIdentifier);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("CancelTransactionCommand for Transaction with identifier [")
                   .appendValue(transactionIdentifier)
                   .appendText("]");
    }
}
