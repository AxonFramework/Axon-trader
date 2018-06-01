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
import org.axonframework.samples.trader.users.command.User;
import org.axonframework.samples.trader.users.util.DigestUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private AggregateTestFixture<User> fixture;

    private UserId userId = new UserId();

    private UserCreatedEvent userCreatedEvent;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(User.class);

        userCreatedEvent = new UserCreatedEvent(userId, "Buyer 1", "buyer1", DigestUtils.sha1("buyer1"));
    }

    @Test
    public void testHandleCreateUser() {
        fixture.givenNoPriorActivity()
               .when(new CreateUserCommand(userId, "Buyer 1", "buyer1", "buyer1"))
               .expectEvents(userCreatedEvent);
    }

    @Test
    public void testHandleAuthenticateUser() {
        fixture.given(userCreatedEvent)
               .when(new AuthenticateUserCommand(userId, "buyer1", "buyer1".toCharArray()))
               .expectEvents(new UserAuthenticatedEvent(userId));
    }
}
