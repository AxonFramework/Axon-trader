package org.axonframework.samples.trader.app.query.user;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class UserEntry {
    private UUID identifier;
    private String name;
    private String username;
    private String password;

    public UUID getIdentifier() {
        return identifier;
    }

    void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
