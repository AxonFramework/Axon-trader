package org.axonframework.samples.trader.query.users;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageHandler;
import org.axonframework.samples.trader.api.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Service
public class UserCommandHandler implements MessageHandler<CommandMessage<?>> {

    private final CommandBus commandBus;

    private CreateUserCommand command;

    @Autowired
    public UserCommandHandler(CommandBus commandBus) {
        this.commandBus = commandBus;
        this.createUser(command);
    }

    @CommandHandler
    public void createUser(CreateUserCommand command) {
        apply(new UserCreatedEvent(command.getUserId(), command.getName(), command.getUsername(), command.getPassword()));
    }

    @CommandHandler
    public void authenticateUser(AuthenticateUserCommand command) {
        apply(new UserAuthenticatedEvent(command.getUserId()));
    }

    @Override
    public Object handle(CommandMessage<?> message) throws Exception {
        return null;
    }
}
