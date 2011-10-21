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

package org.axonframework.samples.trader.app.query;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Helper class to interact with the Mongodb instance.</p>
 * <p>For one database is used. Methods are available to obtain a connection to this database. Other methods are
 * available to obtain a reference to collections immediately for data insertion and querying</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class MongoHelper {
    private MongoTemplate mongoTemplate;

    public DBCollection users() {
        return getDatabase().getCollection("users");
    }

    public DBCollection companies() {
        return getDatabase().getCollection("companies");
    }

    public DBCollection tradesExecuted() {
        return getDatabase().getCollection("tradesexecuted");
    }

    public DBCollection orderBooks() {
        return getDatabase().getCollection("orderbooks");
    }

    public DBCollection orders() {
        return getDatabase().getCollection("orders");
    }

    public DB getDatabase() {
        return mongoTemplate.database();
    }

    @Autowired
    public void setMongoDb(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
