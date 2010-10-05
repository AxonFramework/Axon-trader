package org.axonframework.samples.trader.app.query.tradeitem;

import java.util.List;

/**
 * @author Jettro Coenradie
 */
public interface TradeItemRepository {
    List<TradeItemEntry> listAllTradeItems();

    TradeItemEntry findTradeItemByIdentifier(String tradeItemIdentifier);

    TradeItemEntry findTradeItemByOrderBookIdentifier(String orderBookIdentifier);
}
