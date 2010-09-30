package org.axonframework.samples.trader.app.api.user;

/**
 * @author Jettro Coenradie
 */
public class AuthenticateUserCommand {
    private final String userName;
    private final char[] password;

    public AuthenticateUserCommand(String userName, char[] password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }

}
