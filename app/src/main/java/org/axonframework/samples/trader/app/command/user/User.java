package org.axonframework.samples.trader.app.command.user;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.app.api.user.UserAuthenticatedEvent;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;

import java.util.UUID;

import static org.axonframework.samples.trader.app.util.DigestUtils.sha1;

/**
 * @author Jettro Coenradie
 */
public class User extends AbstractAnnotatedAggregateRoot {
    private String passwordHash;

    public User(UUID identifier, String username, String name, String password) {
        super(identifier);
        apply(new UserCreatedEvent(name,username, hashOf(password.toCharArray())));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public User(UUID identifier) {
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
