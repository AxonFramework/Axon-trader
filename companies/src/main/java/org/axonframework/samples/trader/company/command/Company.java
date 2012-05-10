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

package org.axonframework.samples.trader.company.command;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.samples.trader.company.api.CompanyCreatedEvent;
import org.axonframework.samples.trader.company.api.CompanyId;

/**
 * @author Jettro Coenradie
 */
public class Company extends AbstractAnnotatedAggregateRoot {
    private static final long serialVersionUID = 8723320580782813954L;

    @AggregateIdentifier
    private CompanyId companyId;

    @SuppressWarnings("UnusedDeclaration")
    protected Company() {
    }

    public Company(CompanyId companyId, String name, long value, long amountOfShares) {
        apply(new CompanyCreatedEvent(companyId, name, value, amountOfShares));
    }

    @Override
    public CompanyId getIdentifier() {
        return this.companyId;
    }

    @EventHandler
    public void handle(CompanyCreatedEvent event) {
        this.companyId = event.getCompanyIdentifier();
    }

}
