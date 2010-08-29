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
}
