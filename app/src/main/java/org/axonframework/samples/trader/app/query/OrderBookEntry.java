package org.axonframework.samples.trader.app.query;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Entity
public class OrderBookEntry {
    @Id
    @GeneratedValue
    private Long db_identifier;

    @Basic
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID identifier;

    @Basic(optional = true)
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID tradeItemIdentifier;

    @Basic
    private String tradeItemName;

    @OneToMany(mappedBy = "orderBookEntry")
    private List<OrderEntry> orders;

    public List<OrderEntry> sellOrders() {
        List<OrderEntry> sellOrders = new ArrayList<OrderEntry>();
        for (OrderEntry order : orders) {
            if (order.getType().equalsIgnoreCase("sell")) {
                sellOrders.add(order);
            }
        }
        return sellOrders;
    }

    public List<OrderEntry> buyOrders() {
        List<OrderEntry> buyOrders = new ArrayList<OrderEntry>();
        for (OrderEntry order : orders) {
            if (order.getType().equalsIgnoreCase("buy")) {
                buyOrders.add(order);
            }
        }
        return buyOrders;
    }

    public Long getDb_identifier() {
        return db_identifier;
    }

    void setDb_identifier(Long db_identifier) {
        this.db_identifier = db_identifier;
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

    public List<OrderEntry> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntry> orders) {
        this.orders = orders;
    }
}
