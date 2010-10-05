package org.axonframework.samples.trader.app.query.user;

import org.axonframework.samples.trader.app.api.user.UserAccount;

/**
 * @author Jettro Coenradie
 */
public class UserEntry implements UserAccount {
    private String identifier;
    private String name;
    private String username;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
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
        return this.identifier;
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
