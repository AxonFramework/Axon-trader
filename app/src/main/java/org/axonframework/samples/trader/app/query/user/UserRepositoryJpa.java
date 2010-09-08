package org.axonframework.samples.trader.app.query.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jettro Coenradie
 */
//@Repository
@Transactional(readOnly = true)
public class UserRepositoryJpa implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntry findByUsername(String username) {
        return (UserEntry) entityManager.createQuery("SELECT e FROM UserEntry e where e.username = :username")
                .setParameter("username",username)
                .setMaxResults(250)
                .getSingleResult();

    }
}
