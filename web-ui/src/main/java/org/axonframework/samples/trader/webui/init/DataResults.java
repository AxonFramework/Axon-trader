package org.axonframework.samples.trader.webui.init;

import java.util.List;
import java.util.Map;

/**
 * Created by jettrocoenradie on 19/08/14.
 */
public class DataResults {
    private int totalItems;
    private List<Map> items;

    public DataResults(int totalItems, List<Map> items) {
        this.totalItems = totalItems;
        this.items = items;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public List<Map> getItems() {
        return items;
    }
}
