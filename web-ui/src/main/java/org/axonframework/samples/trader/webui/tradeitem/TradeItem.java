package org.axonframework.samples.trader.webui.tradeitem;

/**
 * @author Jettro Coenradie
 */
public class TradeItem {
    private String name;
    private String identifier;

    public TradeItem(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}
