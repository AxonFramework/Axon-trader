package org.axonframework.samples.trader.app.query.orderbook;

import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public interface OrderBookRepository {
    List<OrderBookEntry> listAllOrderBooks();

    OrderBookEntry findByIdentifier(UUID aggregateIdentifier);

    OrderBookEntry findByTradeItem(UUID tradeItemIdentifier);

    OrderEntry findByOrderIdentifier(UUID orderIdentifier);
}
