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

package org.axonframework.samples.trader.app.command.user;

import org.axonframework.samples.trader.api.users.*;
import org.axonframework.samples.trader.query.users.UserEntry;
import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
import org.axonframework.samples.trader.users.command.User;
import org.axonframework.samples.trader.users.command.UserCommandHandler;
import org.axonframework.samples.trader.users.util.DigestUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Jettro Coenradie
 */
public class UserCommandHandlerTest {

    private AggregateTestFixture<User> fixture;

    private UserQueryRepository userQueryRepository;

    @Before
    public void setUp() {
        userQueryRepository = Mockito.mock(UserQueryRepository.class);

        fixture = new AggregateTestFixture<>(User.class);
        UserCommandHandler commandHandler = new UserCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        commandHandler.setUserRepository(userQueryRepository);
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }


    @Test
    public void testHandleCreateUser() throws Exception {
        UserId aggregateIdentifier = new UserId();
        fixture.given()
                .when(new CreateUserCommand(aggregateIdentifier, "Buyer 1", "buyer1", "buyer1"))
                .expectEvents(new UserCreatedEvent(aggregateIdentifier, "Buyer 1", "buyer1", DigestUtils.sha1("buyer1")));
    }

    @Test
    public void testHandleAuthenticateUser() throws Exception {
        UserId aggregateIdentifier = new UserId();

        UserEntry userEntry = new UserEntry();
        userEntry.setUsername("buyer1");
        userEntry.setIdentifier(aggregateIdentifier.toString());
        userEntry.setName("Buyer One");
        Mockito.when(userQueryRepository.findByUsername("buyer1")).thenReturn(userEntry);

        fixture.given(new UserCreatedEvent(aggregateIdentifier, "Buyer 1", "buyer1", DigestUtils.sha1("buyer1")))
                .when(new AuthenticateUserCommand("buyer1", "buyer1".toCharArray()))
                .expectEvents(new UserAuthenticatedEvent(aggregateIdentifier));
    }
}
