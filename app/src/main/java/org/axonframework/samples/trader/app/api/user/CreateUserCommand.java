package org.axonframework.samples.trader.app.api.user;

/**
 * @author Jettro Coenradie
 */
public class CreateUserCommand {
    private String username;
    private String name;
    private String password;

    public CreateUserCommand(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
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
