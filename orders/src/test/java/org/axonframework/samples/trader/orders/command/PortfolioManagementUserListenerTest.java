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

package org.axonframework.samples.trader.orders.command;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.axonframework.samples.trader.api.users.UserId;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PortfolioManagementUserListenerTest {

    private final CommandGateway commandGateway = mock(CommandGateway.class);

    private final PortfolioManagementUserListener listener = new PortfolioManagementUserListener(commandGateway);

    @Test
    public void checkPortfolioCreationAfterUserCreated() {
        UserId userId = new UserId();

        listener.on(new UserCreatedEvent(userId, "Test", "testuser", "testpassword"));

        verify(commandGateway).send(argThat(new CreatePortfolioCommandMatcher(userId)));
    }

    // TODO #28 replace this by a direct command equals call. This requires instantiating the aggregate ids ourselves
    private class CreatePortfolioCommandMatcher extends ArgumentMatcher<CreatePortfolioCommand> {

        private UserId userId;

        private CreatePortfolioCommandMatcher(UserId userId) {
            this.userId = userId;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof CreatePortfolioCommand)) {
                return false;
            }

            CreatePortfolioCommand createPortfolioCommand = (CreatePortfolioCommand) argument;
            return createPortfolioCommand.getUserId().equals(userId);
        }
    }
}
