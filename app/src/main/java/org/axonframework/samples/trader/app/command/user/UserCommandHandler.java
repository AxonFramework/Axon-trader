package org.axonframework.samples.trader.app.command.user;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
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

    @CommandHandler
    public UUID handleCreateUser(CreateUserCommand command) {
        UUID identifier = UUID.randomUUID();
        User user = new User(identifier, command.getUsername(), command.getName(), command.getPassword());
        repository.add(user);
        return identifier;
    }

    @Autowired
    @Qualifier("userRepository")
    public void setRepository(Repository<User> userRepository) {
        this.repository = userRepository;
    }

}
