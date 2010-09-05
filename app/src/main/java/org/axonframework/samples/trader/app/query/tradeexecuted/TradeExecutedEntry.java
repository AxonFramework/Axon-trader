package org.axonframework.samples.trader.app.query.tradeexecuted;

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
public class TradeExecutedEntry {
    @Id
    @GeneratedValue
    private Long db_identifier;

    @Basic
    private long tradeCount;

    @Basic
    private int tradePrice;

    @Basic
    private String tradeItemName;

    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID orderBookIdentifier;

    public Long getDb_identifier() {
        return db_identifier;
    }

    public void setDb_identifier(Long db_identifier) {
        this.db_identifier = db_identifier;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    public void setTradeItemName(String tradeItemName) {
        this.tradeItemName = tradeItemName;
    }

    public int getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(int tradePrice) {
        this.tradePrice = tradePrice;
    }

    public UUID getOrderBookIdentifier() {
        return orderBookIdentifier;
    }

    public void setOrderBookIdentifier(UUID orderBookIdentifier) {
        this.orderBookIdentifier = orderBookIdentifier;
    }
}
