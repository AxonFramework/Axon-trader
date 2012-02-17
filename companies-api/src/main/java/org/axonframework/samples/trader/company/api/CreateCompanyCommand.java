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

import org.axonframework.domain.AggregateIdentifier;

/**
 * <p>Create a new company by proving the name, the estiamted value of the company and the amount of shares that are
 * available for the company. You also must provide the id of the user that wants to create the company.</p>
 *
 * @author Jettro Coenradie
 */
public class CreateCompanyCommand {

    private AggregateIdentifier userId;
    private String companyName;
    private long companyValue;
    private long amountOfShares;

    public CreateCompanyCommand(AggregateIdentifier userId, String companyName, long companyValue,
                                long amountOfShares) {
        this.amountOfShares = amountOfShares;
        this.companyName = companyName;
        this.companyValue = companyValue;
        this.userId = userId;
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

    public AggregateIdentifier getUserId() {
        return userId;
    }
}
