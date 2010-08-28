package org.axonframework.samples.trader.app.query.user;

/**
 * @author Jettro Coenradie
 */
public interface UserRepository {
    UserEntry findByUsername(String username);
}
