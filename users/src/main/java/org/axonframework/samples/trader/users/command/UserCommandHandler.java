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

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.StringAggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
import org.axonframework.samples.trader.users.api.AuthenticateUserCommand;
import org.axonframework.samples.trader.users.api.CreateUserCommand;
import org.axonframework.samples.trader.users.api.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class UserCommandHandler {

    private Repository<User> repository;

    private UserQueryRepository userQueryRepository;

    @CommandHandler
    public AggregateIdentifier handleCreateUser(CreateUserCommand command) {
        AggregateIdentifier identifier = new UUIDAggregateIdentifier();
        User user = new User(identifier, command.getUsername(), command.getName(), command.getPassword());
        repository.add(user);
        return identifier;
    }

    @CommandHandler
    public UserAccount handleAuthenticateUser(AuthenticateUserCommand command) {
        UserAccount account = userQueryRepository.findByUsername(command.getUserName());
        if (account == null) {
            return null;
        }
        boolean success = onUser(account.getUserId()).authenticate(command.getPassword());
        return success ? account : null;
    }

    private User onUser(String userId) {
        return repository.load(new StringAggregateIdentifier(userId), null);
    }


    @Autowired
    @Qualifier("userRepository")
    public void setRepository(Repository<User> userRepository) {
        this.repository = userRepository;
    }

    @Autowired
    public void setUserRepository(UserQueryRepository userRepository) {
        this.userQueryRepository = userRepository;
    }
}
