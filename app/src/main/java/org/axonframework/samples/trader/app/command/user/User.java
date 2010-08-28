package org.axonframework.samples.trader.app.command.user;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class User extends AbstractAnnotatedAggregateRoot {

    public User(UUID identifier, String username, String name) {
        super(identifier);
        apply(new UserCreatedEvent(name,username));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public User(UUID identifier) {
        super(identifier);
    }

    @EventHandler
    public void onUserCreated(UserCreatedEvent event) {
        // nothing for now
    }

}
