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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.company.api.CompanyCreatedEvent;
import org.axonframework.samples.trader.company.api.CreateCompanyCommand;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.*;

/**
 * @author Jettro Coenradie
 */
public class CompanyCommandHandlerTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        CompanyCommandHandler commandHandler = new CompanyCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(Company.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testCreateCompany() {
        AggregateIdentifier userId = new UUIDAggregateIdentifier();
        CreateCompanyCommand command = new CreateCompanyCommand(userId, "TestItem", 1000, 10000);

        fixture.given()
               .when(command)
               .expectEvents(new CompanyCreatedEvent("TestItem", 1000, 10000));
    }
}
