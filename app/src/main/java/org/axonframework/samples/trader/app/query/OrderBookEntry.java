package org.axonframework.samples.trader.app.query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class OrderBookEntry {
    private UUID identifier;
    private UUID tradeItemIdentifier;
    private String tradeItemName;
    private List<OrderEntry> sellOrders = new ArrayList<OrderEntry>();
    private List<OrderEntry> buyOrders = new ArrayList<OrderEntry>();

    public List<OrderEntry> sellOrders() {
        return sellOrders;
    }

    public List<OrderEntry> buyOrders() {
        return buyOrders;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public UUID getTradeItemIdentifier() {
        return tradeItemIdentifier;
    }

    void setTradeItemIdentifier(UUID tradeItemIdentifier) {
        this.tradeItemIdentifier = tradeItemIdentifier;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    void setTradeItemName(String tradeItemName) {
        this.tradeItemName = tradeItemName;
    }

    public List<OrderEntry> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<OrderEntry> buyOrders) {
        this.buyOrders = buyOrders;
    }

    public List<OrderEntry> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<OrderEntry> sellOrders) {
        this.sellOrders = sellOrders;
    }
}
