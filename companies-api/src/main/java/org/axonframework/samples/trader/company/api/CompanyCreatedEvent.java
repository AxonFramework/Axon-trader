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

package org.axonframework.samples.trader.company.api;

/**
 * <p>A new company is created with a certain value and an amount of shares. Those two values can be used to calculate
 * the starting point for the value of a share.</p>
 *
 * @author Jettro Coenradie
 */
public class CompanyCreatedEvent {
    private CompanyId companyId;
    private String companyName;
    private long companyValue;
    private long amountOfShares;

    public CompanyCreatedEvent(CompanyId companyId, String companyName, long amountOfShares, long companyValue) {
        this.amountOfShares = amountOfShares;
        this.companyName = companyName;
        this.companyValue = companyValue;
        this.companyId = companyId;
    }

    public CompanyId getCompanyIdentifier() {
        return this.companyId;
    }

    public long getAmountOfShares() {
        return amountOfShares;
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getCompanyValue() {
        return companyValue;
    }
}
