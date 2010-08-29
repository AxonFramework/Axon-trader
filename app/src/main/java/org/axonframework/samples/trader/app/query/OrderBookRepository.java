package org.axonframework.samples.trader.app.query;

import java.util.List;

/**
 * @author Jettro Coenradie
 */
public interface OrderBookRepository {
    List<OrderBookEntry> listAllOrderBooks();


}
