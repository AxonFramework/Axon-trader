package org.axonframework.samples.trader.app.query.tradeexecuted;

import java.util.List;

/**
 * @author Jettro Coenradie
 */
public interface TradeExecutedRepository {
    List<TradeExecutedEntry> findExecutedTradesForOrderBook(String orderBookIdentifier);
}
