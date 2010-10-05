package org.axonframework.samples.trader.app.query.user;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class UserListener {
    private MongoHelper mongo;

    @EventHandler
    public void handleUserCreated(UserCreatedEvent event) {
        DBObject userEntry = BasicDBObjectBuilder.start()
                .add("identifier", event.getUserIdentifier().asString())
                .add("name", event.getName())
                .add("username", event.getUsername())
                .add("password", event.getPassword())
                .get();

        mongo.users().insert(userEntry);
    }

    @Autowired
    public void setMongoHelper(MongoHelper mongoHelper) {
        this.mongo = mongoHelper;
    }
}
