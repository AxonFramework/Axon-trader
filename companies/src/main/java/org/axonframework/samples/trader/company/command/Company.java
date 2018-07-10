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

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.samples.trader.api.company.*;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate(snapshotTriggerDefinition = "defaultSnapshotTriggerDefinition")
public class Company {

    @AggregateIdentifier
    private CompanyId companyId;

    @SuppressWarnings("UnusedDeclaration")
    public Company() {
        // Required by Axon Framework
    }

    @CommandHandler
    public Company(CreateCompanyCommand cmd) {
        apply(new CompanyCreatedEvent(
                cmd.getCompanyId(), cmd.getCompanyName(), cmd.getCompanyValue(), cmd.getAmountOfShares()
        ));
    }

    @CommandHandler
    public void handle(AddOrderBookToCompanyCommand cmd) {
        apply(new OrderBookAddedToCompanyEvent(companyId, cmd.getOrderBookId()));
    }


    @EventSourcingHandler
    public void on(CompanyCreatedEvent event) {
        companyId = event.getCompanyId();
    }
}
