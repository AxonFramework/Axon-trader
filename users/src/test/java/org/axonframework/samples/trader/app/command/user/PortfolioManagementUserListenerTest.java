/*
 * Copyright (c) 2011. Gridshore
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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.axonframework.samples.trader.orders.api.portfolio.CreatePortfolioCommand;
import org.axonframework.test.utils.DomainEventUtils;
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

        UserCreatedEvent event = new UserCreatedEvent("Test", "testuser", "testpassword");
        AggregateIdentifier userIdentifier = new UUIDAggregateIdentifier();
        DomainEventUtils.setAggregateIdentifier(event, userIdentifier);

        listener.createNewPortfolioWhenUserIsCreated(event);

        Mockito.verify(commandBus).dispatch(Matchers.argThat(new UserIdentifierMatcher(userIdentifier.asString())));
    }

    private class UserIdentifierMatcher extends ArgumentMatcher {
        private String identifier;

        private UserIdentifierMatcher(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof CreatePortfolioCommand)) {
                return false;
            }
            CreatePortfolioCommand createPortfolioCommand = (CreatePortfolioCommand) argument;
            return createPortfolioCommand.getUserIdentifier().asString().equals(identifier);
        }
    }
}
