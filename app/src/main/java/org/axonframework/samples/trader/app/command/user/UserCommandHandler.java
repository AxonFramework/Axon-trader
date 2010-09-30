package org.axonframework.samples.trader.app.command.user;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.user.AuthenticateUserCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.api.user.UserAccount;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.UserRepository;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Component
public class UserCommandHandler {
    private Repository<User> repository;

    private UserRepository userQueryRepository;

    @CommandHandler
    public UUID handleCreateUser(CreateUserCommand command) {
        UUID identifier = UUID.randomUUID();
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
        return repository.load(UUID.fromString(userId), null);
    }


    @Autowired
    @Qualifier("userRepository")
    public void setRepository(Repository<User> userRepository) {
        this.repository = userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userQueryRepository = userRepository;
    }
}
