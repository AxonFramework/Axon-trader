package org.axonframework.samples.trader.app.query;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Entity
public class OrderEntry {
    @Id
    @GeneratedValue
    private Long db_identifier;

    @Basic
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID identifier;

    @ManyToOne(fetch = FetchType.EAGER)
    private OrderBookEntry orderBookEntry;

    @Basic
    private long tradeCount;

    @Basic
    private int itemPrice;

    @Basic
    private UUID userId;

    @Basic
    private long itemsRemaining;

    @Basic
    private String type;

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

    public int getItemPrice() {
        return itemPrice;
    }

    void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    void setItemsRemaining(long itemsRemaining) {
        this.itemsRemaining = itemsRemaining;
    }

    public OrderBookEntry getOrderBookEntry() {
        return orderBookEntry;
    }

    void setOrderBookEntry(OrderBookEntry orderBookEntry) {
        this.orderBookEntry = orderBookEntry;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public UUID getUserId() {
        return userId;
    }

    void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }
}
