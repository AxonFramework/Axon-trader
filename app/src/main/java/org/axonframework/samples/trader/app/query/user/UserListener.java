package org.axonframework.samples.trader.app.query.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jettro Coenradie
 */
@Component
public class UserListener {
    @PersistenceContext
    private EntityManager entityManager;

    private Mongo mongoDb;

    @EventHandler
    public void handleUserCreated(UserCreatedEvent event) {
        UserEntry entry= new UserEntry();
        entry.setIdentifier(event.getUserIdentifier());
        entry.setName(event.getName());
        entry.setUsername(event.getUsername());
        entityManager.persist(entry);


        BasicDBObject userEntry = new BasicDBObject();
        userEntry.put("identifier",event.getUserIdentifier());
        userEntry.put("name", event.getName());
        userEntry.put("username", event.getUsername());

        mongoDb.getDB("axontrader").getCollection("users").insert(userEntry);
    }

    @Autowired
    public void setMongoDb(Mongo mongoDb) {
        this.mongoDb = mongoDb;
    }
}
