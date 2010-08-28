package org.axonframework.samples.trader.app.api.user;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class CreateUserCommand {
    private String username;
    private String name;

    public CreateUserCommand(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
