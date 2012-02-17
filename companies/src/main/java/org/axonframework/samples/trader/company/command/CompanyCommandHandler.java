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

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.company.api.CreateCompanyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Jettro Coenradie
 */
public class CompanyCommandHandler {

    private Repository<Company> repository;

    @CommandHandler
    public void handleCreateCompany(CreateCompanyCommand command) {
        Company company = new Company(new UUIDAggregateIdentifier(),
                                      command.getCompanyName(),
                                      command.getCompanyValue(),
                                      command.getAmountOfShares());
        repository.add(company);
    }

    @Autowired
    @Qualifier("companyRepository")
    public void setRepository(Repository<Company> companyRepository) {
        this.repository = companyRepository;
    }
}
