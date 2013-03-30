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

package org.axonframework.samples.trader.api.users;

import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Command to create a new user.
 *
 * @author Jettro Coenradie
 */
public class CreateUserCommand {
    private UserId userId;
    @NotNull
    @Size(min = 3)
    private String username;
    private String name;
    @NotNull
    @Size(min = 3)
    private String password;

    public CreateUserCommand(UserId userId, String name, String username, String password) {
        Assert.notNull(userId, "The provided userId cannot be null");
        Assert.notNull(name, "The provided name cannot be null");
        Assert.notNull(username, "The provided username cannot be null");
        Assert.notNull(password, "The provided password cannot be null");

        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
