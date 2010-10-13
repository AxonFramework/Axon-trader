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

package org.axonframework.samples.trader.app.api.tradeitem;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public class TradeItemCreatedEvent extends DomainEvent {
    private String tradeItemName;
    private long tradeItemValue;
    private long amountOfShares;

    public TradeItemCreatedEvent(String tradeItemName, long amountOfShares, long tradeItemValue) {
        this.amountOfShares = amountOfShares;
        this.tradeItemName = tradeItemName;
        this.tradeItemValue = tradeItemValue;
    }

    public AggregateIdentifier getTradeItemIdentifier() {
        return getAggregateIdentifier();
    }

    public long getAmountOfShares() {
        return amountOfShares;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    public long getTradeItemValue() {
        return tradeItemValue;
    }

}
