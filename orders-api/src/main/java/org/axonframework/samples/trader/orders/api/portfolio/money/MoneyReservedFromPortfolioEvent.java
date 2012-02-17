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

package org.axonframework.samples.trader.orders.api.portfolio.money;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public class MoneyReservedFromPortfolioEvent extends DomainEvent {

    private AggregateIdentifier transactionIdentifier;
    private long amountToReserve;

    public MoneyReservedFromPortfolioEvent(AggregateIdentifier transactionIdentifier, long amountToReserve) {
        this.transactionIdentifier = transactionIdentifier;
        this.amountToReserve = amountToReserve;
    }

    public AggregateIdentifier getPortfolioIdentifier() {
        return super
                .getAggregateIdentifier();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long getAmountToReserve() {
        return amountToReserve;
    }

    public AggregateIdentifier getTransactionIdentifier() {
        return transactionIdentifier;
    }
}
