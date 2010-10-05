package org.axonframework.samples.trader.app.query.tradeexecuted;

import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public interface TradeExecutedRepository {
    List<TradeExecutedEntry> findExecutedTradesForOrderBook(String orderBookIdentifier);
}
