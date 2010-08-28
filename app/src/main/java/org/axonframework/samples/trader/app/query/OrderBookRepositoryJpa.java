package org.axonframework.samples.trader.app.query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Repository
@Transactional(readOnly = true)
public class OrderBookRepositoryJpa implements OrderBookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings({"unchecked"})
    @Override
    public List<OrderBookEntry> listAllOrderBooks() {
        return entityManager.createQuery("SELECT e FROM OrderBookEntry e")
                .setMaxResults(250)
                .getResultList();
    }
}
