/*
 * Copyright (c) 2010. Gridshore
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.app.query.user;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

        return mapToUserEntry(one);
    }

    @Override
    public List<UserEntry> obtainAllUsers() {
        DBCursor dbCursor = mongo.users().find();
        List<UserEntry> users = new ArrayList<UserEntry>(dbCursor.size());
        while (dbCursor.hasNext()) {
            users.add(mapToUserEntry(dbCursor.next()));
        }
        return users;
    }

    private UserEntry mapToUserEntry(DBObject one) {
        UserEntry entry = new UserEntry();
        entry.setIdentifier((String) one.get("identifier"));
        entry.setName((String) one.get("name"));
        entry.setUsername((String) one.get("username"));
        return entry;
    }

    @Autowired
    public void setMongohelper(MongoHelper mongoHelper) {
        this.mongo = mongoHelper;
    }
}
