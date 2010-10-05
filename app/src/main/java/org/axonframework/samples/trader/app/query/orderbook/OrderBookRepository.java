package org.axonframework.samples.trader.app.query.orderbook;

import java.util.List;

/**
 * @author Jettro Coenradie
 */
public interface OrderBookRepository {
    List<OrderBookEntry> listAllOrderBooks();

    OrderBookEntry findByIdentifier(String aggregateIdentifier);

    OrderBookEntry findByTradeItem(String tradeItemIdentifier);

    OrderEntry findByOrderIdentifier(String orderIdentifier);
}
