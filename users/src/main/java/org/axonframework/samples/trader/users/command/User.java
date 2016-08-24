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

package org.axonframework.samples.trader.users.command;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.users.UserAuthenticatedEvent;
import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.samples.trader.users.util.DigestUtils;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * @author Jettro Coenradie
 */
@AggregateRoot
public class User {
    private static final long serialVersionUID = 3291411359839192350L;
    @AggregateIdentifier
    private UserId userId;
    private String passwordHash;

    public User() {
    }

    public User(UserId userId, String username, String name, String password) {
        apply(new UserCreatedEvent(userId, name, username, hashOf(password.toCharArray())));
    }

    public boolean authenticate(char[] password) {
        boolean success = this.passwordHash.equals(hashOf(password));
        if (success) {
            apply(new UserAuthenticatedEvent(userId));
        }
        return success;
    }

    @EventHandler
    public void onUserCreated(UserCreatedEvent event) {
        this.userId = event.getUserIdentifier();
        this.passwordHash = event.getPassword();
    }

    private String hashOf(char[] password) {
        return DigestUtils.sha1(String.valueOf(password));
    }

    public UserId getIdentifier() {
        return userId;
    }
}
