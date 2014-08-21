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

package org.axonframework.samples.trader.query.company;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;

/**
 * @author Jettro Coenradie
 */
@Entity
public class CompanyEntry {

    @Id
    @javax.persistence.Id
    private String identifier;
    private String name;
    private long value;
    private long amountOfShares;
    private boolean tradeStarted;

    public long getAmountOfShares() {
        return amountOfShares;
    }

    public void setAmountOfShares(long amountOfShares) {
        this.amountOfShares = amountOfShares;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTradeStarted() {
        return tradeStarted;
    }

    public void setTradeStarted(boolean tradeStarted) {
        this.tradeStarted = tradeStarted;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
