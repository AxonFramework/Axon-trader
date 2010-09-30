package org.axonframework.samples.trader.app.query.user;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Repository
public class UserRepositoryMongo implements UserRepository {
    private MongoHelper mongo;

    @Override
    public UserEntry findByUsername(String username) {
        DBObject query = BasicDBObjectBuilder.start("username", username).get();
        DBObject one = mongo.users().findOne(query);

        if (null == one) {
            return null;
        }

        UserEntry entry = new UserEntry();
        entry.setIdentifier((UUID) one.get("identifier"));
        entry.setName((String) one.get("name"));
        entry.setUsername((String) one.get("username"));
        return entry;
    }

    @Autowired
    public void setMongohelper(MongoHelper mongoHelper) {
        this.mongo = mongoHelper;
    }
}
