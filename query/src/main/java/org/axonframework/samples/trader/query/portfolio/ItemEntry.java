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

package org.axonframework.samples.trader.query.portfolio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class ItemEntry {

    @javax.persistence.Id
    @GeneratedValue
    private Long generatedId;

    private String identifier; // OrderBook identifier
    private String companyIdentifier;
    private String companyName;
    private long amount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCompanyIdentifier() {
        return companyIdentifier;
    }

    public void setCompanyIdentifier(String companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
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

    @Override
    public String toString() {
        return "ItemEntry{" +
                "amount=" + amount +
                ", identifier='" + identifier + '\'' +
                ", companyIdentifier='" + companyIdentifier + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }

    public Long getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(Long generatedId) {
        this.generatedId = generatedId;
    }
}
