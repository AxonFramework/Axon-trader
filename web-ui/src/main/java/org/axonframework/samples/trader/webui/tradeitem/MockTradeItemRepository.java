package org.axonframework.samples.trader.webui.tradeitem;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Repository
public class MockTradeItemRepository implements TradeItemRepository {
    public List<TradeItem> list() {
        ArrayList<TradeItem> tradeItems = new ArrayList<TradeItem>();
        tradeItems.add(new TradeItem("ident1", "TradeItem1"));
        tradeItems.add(new TradeItem("ident2", "TradeItem2"));
        tradeItems.add(new TradeItem("ident3", "TradeItem3"));
        tradeItems.add(new TradeItem("ident4", "TradeItem4"));
        tradeItems.add(new TradeItem("ident5", "TradeItem5"));
        return tradeItems;
    }
}
