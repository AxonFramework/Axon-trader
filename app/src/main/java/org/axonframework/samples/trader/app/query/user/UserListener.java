package org.axonframework.samples.trader.app.query.user;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jettro Coenradie
 */
@Component
public class UserListener {
    @PersistenceContext
    private EntityManager entityManager;

    @EventHandler
    public void handleUserCreated(UserCreatedEvent event) {
        UserEntry entry= new UserEntry();
        entry.setIdentifier(event.getUserIdentifier());
        entry.setName(event.getName());
        entry.setUsername(event.getUsername());
        entityManager.persist(entry);
    }
}
