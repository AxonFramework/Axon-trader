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
public class TradeItemEntry {
    private UUID identifier;
    private UUID orderBookIdentifier;
    private String name;
    private long value;
    private long amountOfShares;
    private boolean tradeStarted;

    public long getAmountOfShares() {
        return amountOfShares;
    }

    void setAmountOfShares(long amountOfShares) {
        this.amountOfShares = amountOfShares;
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
