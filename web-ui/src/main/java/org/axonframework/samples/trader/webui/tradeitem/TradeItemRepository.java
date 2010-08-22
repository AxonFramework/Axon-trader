package org.axonframework.samples.trader.webui.tradeitem;

import java.util.List;

/**
 * @author Jettro Coenradie
 */
public interface TradeItemRepository {
    List<TradeItem> list();
}
