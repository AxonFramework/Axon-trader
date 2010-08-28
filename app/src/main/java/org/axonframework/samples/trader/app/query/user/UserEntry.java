package org.axonframework.samples.trader.app.query.user;

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
public class UserEntry {
    @Id
    @GeneratedValue
    private Long db_identifier;

    @Basic
    @Type(type = "org.axonframework.samples.trader.app.query.UUIDUserType")
    private UUID identifier;

    @Basic
    private String name;

    @Basic
    private String username;

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

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }
}
