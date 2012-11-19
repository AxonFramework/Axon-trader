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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.samples.trader.api.portfolio.CreatePortfolioCommand;
import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.axonframework.samples.trader.api.users.UserId;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Jettro Coenradie
 */
public class PortfolioManagementUserListenerTest {

    @Test
    public void checkPortfolioCreationAfterUserCreated() {
        CommandBus commandBus = Mockito.mock(CommandBus.class);
        PortfolioManagementUserListener listener = new PortfolioManagementUserListener();
        listener.setCommandBus(commandBus);

        UserId userIdentifier = new UserId();
        UserCreatedEvent event = new UserCreatedEvent(userIdentifier, "Test", "testuser", "testpassword");

        listener.createNewPortfolioWhenUserIsCreated(event);

        Mockito.verify(commandBus).dispatch(Matchers.argThat(new GenericCommandMessageMatcher(userIdentifier)));
    }

    private class GenericCommandMessageMatcher extends ArgumentMatcher<GenericCommandMessage> {

        private UserId userId;

        private GenericCommandMessageMatcher(UserId userId) {
            this.userId = userId;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof GenericCommandMessage)) {
                return false;
            }
            if (!(((GenericCommandMessage) argument).getPayload() instanceof CreatePortfolioCommand)) {
                return false;
            }
            CreatePortfolioCommand createPortfolioCommand = ((GenericCommandMessage<CreatePortfolioCommand>) argument).getPayload();
            return createPortfolioCommand.getUserId().equals(userId);
        }
    }

}
