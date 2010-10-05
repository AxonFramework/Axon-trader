package org.axonframework.samples.trader.app.api.user;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public class UserCreatedEvent extends DomainEvent {
    private String username;
    private String name;
    private String password;

    public UserCreatedEvent(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public AggregateIdentifier getUserIdentifier() {
        return getAggregateIdentifier();
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
