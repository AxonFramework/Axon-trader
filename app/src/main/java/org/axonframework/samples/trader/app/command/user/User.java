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

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.user.UserAuthenticatedEvent;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;

import static org.axonframework.samples.trader.app.util.DigestUtils.sha1;

/**
 * @author Jettro Coenradie
 */
public class User extends AbstractAnnotatedAggregateRoot {
    private String passwordHash;

    public User(AggregateIdentifier identifier, String username, String name, String password) {
        super(identifier);
        apply(new UserCreatedEvent(name, username, hashOf(password.toCharArray())));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public User(AggregateIdentifier identifier) {
        super(identifier);
    }

    public boolean authenticate(char[] password) {
        boolean success = this.passwordHash.equals(hashOf(password));
        if (success) {
            apply(new UserAuthenticatedEvent());
        }
        return success;
    }

    @EventHandler
    public void onUserCreated(UserCreatedEvent event) {
        this.passwordHash = event.getPassword();
    }

    @EventHandler
    public void onUserAuthenticated(UserAuthenticatedEvent event) {
        // nothing for now
    }

    private String hashOf(char[] password) {
        return sha1(String.valueOf(password));
    }

}
