package org.axonframework.samples.trader.app.api.user;

import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class UserCreatedEvent extends DomainEvent {
    private String username;
    private String name;

    public UserCreatedEvent(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public UUID getUserIdentifier() {
        return getAggregateIdentifier();
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
