package org.axonframework.samples.trader.app.query.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Repository
public class UserRepositoryMongo implements UserRepository {
    private Mongo mongo;

    @Override
    public UserEntry findByUsername(String username) {
        BasicDBObject query = new BasicDBObject("username",username);
        DBObject one = mongo.getDB("axontrader").getCollection("users").findOne(query);
        UserEntry entry = new UserEntry();
        entry.setIdentifier((UUID) one.get("identifier"));
        entry.setName((String) one.get("name"));
        entry.setUsername((String) one.get("username"));
        return entry;
    }

    @Autowired
    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }
}
