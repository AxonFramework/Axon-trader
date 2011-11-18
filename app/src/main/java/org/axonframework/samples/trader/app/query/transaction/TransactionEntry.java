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

package org.axonframework.samples.trader.app.query.transaction;

import org.axonframework.samples.trader.app.command.trading.TransactionType;

/**
 * @author Jettro Coenradie
 */
public class TransactionEntry {
    private String identifier;
    private String orderbookIdentifier;
    private String portfolioIdentifier;

    private String companyName;
    private int amountOfItems;
    private int amountOfExecutedItems;
    private long pricePerItem;
    private TransactionState state;
    private TransactionType type;

    public int getAmountOfExecutedItems() {
        return amountOfExecutedItems;
    }

    public void setAmountOfExecutedItems(int amountOfExecutedItems) {
        this.amountOfExecutedItems = amountOfExecutedItems;
    }

    public int getAmountOfItems() {
        return amountOfItems;
    }

    public void setAmountOfItems(int amountOfItems) {
        this.amountOfItems = amountOfItems;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOrderbookIdentifier() {
        return orderbookIdentifier;
    }

    public void setOrderbookIdentifier(String orderbookIdentifier) {
        this.orderbookIdentifier = orderbookIdentifier;
    }

    public String getPortfolioIdentifier() {
        return portfolioIdentifier;
    }

    public void setPortfolioIdentifier(String portfolioIdentifier) {
        this.portfolioIdentifier = portfolioIdentifier;
    }

    public long getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(long pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
