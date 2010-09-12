package org.axonframework.samples.trader.app.query.tradeitem;

import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public interface TradeItemRepository {
    List<TradeItemEntry> listAllTradeItems();

    TradeItemEntry findTradeItemByIdentifier(UUID tradeItemIdentifier);

    TradeItemEntry findTradeItemByOrderBookIdentifier(UUID orderBookIdentifier);
}
