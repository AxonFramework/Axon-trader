package org.axonframework.samples.trader.app.query.user;

import org.axonframework.samples.trader.app.api.user.UserAccount;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class UserEntry implements UserAccount {
    private UUID identifier;
    private String name;
    private String username;

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUserId() {
        return this.identifier.toString();
    }

    @Override
    public String getUserName() {
        return this.username;
    }

    @Override
    public String getFullName() {
        return this.name;
    }
}
