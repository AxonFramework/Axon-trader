package org.axonframework.samples.trader.app.query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Repository
@Transactional(readOnly = true)
public class TradeItemRepositoryJpa implements TradeItemRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings({"unchecked"})
    @Override
    public List<TradeItemEntry> listAllTradeItems() {
        return entityManager.createQuery("SELECT e FROM TradeItemEntry e")
                .setMaxResults(250)
                .getResultList();
    }

    @Override
    public TradeItemEntry findTradeItemByIdentifier(UUID tradeItemIdentifier) {
        return (TradeItemEntry) entityManager.createQuery("SELECT e FROM TradeItemEntry e where e.identifier = :tradeItemIdentifier")
                .setParameter("tradeItemIdentifier",tradeItemIdentifier)
                .getSingleResult();
    }

}
