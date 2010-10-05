package org.axonframework.samples.trader.app.query.tradeitem;

import org.axonframework.domain.AggregateIdentifier;

import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public interface TradeItemRepository {
    List<TradeItemEntry> listAllTradeItems();

    TradeItemEntry findTradeItemByIdentifier(String tradeItemIdentifier);

    TradeItemEntry findTradeItemByOrderBookIdentifier(String orderBookIdentifier);
}
