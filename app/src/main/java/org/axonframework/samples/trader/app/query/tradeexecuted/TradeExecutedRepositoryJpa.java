package org.axonframework.samples.trader.app.query.tradeexecuted;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Repository
public class TradeExecutedRepositoryJpa implements TradeExecutedRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings({"unchecked"})
    @Override
    public List<TradeExecutedEntry> findExecutedTradesForOrderBook(UUID orderBookIdentifier) {
        return entityManager.createQuery("SELECT e FROM TradeExecutedEntry e where e.orderBookIdentifier = :orderBookIdentifier")
                .setParameter("orderBookIdentifier", orderBookIdentifier)
                .setMaxResults(250)
                .getResultList();
    }
}
