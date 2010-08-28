package org.axonframework.samples.trader.app.query;

import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Entity
public class TradeItemEntry {
    @Id
    @GeneratedValue
    private Long db_identifier;

    @Basic
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID identifier;

    @Basic
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID orderBookIdentifier;

    @Basic
    private String name;

    @Basic
    private long value;

    @Basic
    private long amountOfShares;

    @Basic
    private boolean tradeStarted;

    public long getAmountOfShares() {
        return amountOfShares;
    }

    void setAmountOfShares(long amountOfShares) {
        this.amountOfShares = amountOfShares;
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

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean isTradeStarted() {
        return tradeStarted;
    }

    void setTradeStarted(boolean tradeStarted) {
        this.tradeStarted = tradeStarted;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public UUID getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    void setOrderBookIdentifier(UUID orderBookIdentifier) {
        this.orderBookIdentifier = orderBookIdentifier;
    }
}
