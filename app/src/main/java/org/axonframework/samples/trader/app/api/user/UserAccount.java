package org.axonframework.samples.trader.app.api.user;

/**
 * Object used to obtain information about an available UserAccount
 *
 * @author Jettro Coenradie
 */
public interface UserAccount {
    String getUserId();

    String getUserName();

    String getFullName();
}
