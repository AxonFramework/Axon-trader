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

package org.axonframework.samples.trader.app.command.user;

import org.axonframework.samples.trader.app.api.user.AuthenticateUserCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.api.user.UserAuthenticatedEvent;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.repositories.UserQueryRepository;
import org.axonframework.samples.trader.app.util.DigestUtils;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Jettro Coenradie
 */
public class UserCommandHandlerTest {
    private FixtureConfiguration fixture;

    private UserQueryRepository userQueryRepository;

    @Before
    public void setUp() {
        userQueryRepository = Mockito.mock(UserQueryRepository.class);

        fixture = Fixtures.newGivenWhenThenFixture();
        UserCommandHandler commandHandler = new UserCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(User.class));
        commandHandler.setUserRepository(userQueryRepository);
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }


    @Test
    public void testHandleCreateUser() throws Exception {
        fixture.given()
                .when(new CreateUserCommand("Buyer 1", "buyer1", "buyer1"))
                .expectEvents(new UserCreatedEvent("Buyer 1", "buyer1", DigestUtils.sha1("buyer1")));
    }

    @Test
    public void testHandleAuthenticateUser() throws Exception {
        UserEntry userEntry = new UserEntry();
        userEntry.setUsername("buyer1");
        userEntry.setIdentifier(fixture.getAggregateIdentifier().asString());
        userEntry.setName("Buyer One");
        Mockito.when(userQueryRepository.findByUsername("buyer1")).thenReturn(userEntry);

        fixture.given(new UserCreatedEvent("Buyer 1", "buyer1", DigestUtils.sha1("buyer1")))
                .when(new AuthenticateUserCommand("buyer1", "buyer1".toCharArray()))
                .expectEvents(new UserAuthenticatedEvent());
    }
}
