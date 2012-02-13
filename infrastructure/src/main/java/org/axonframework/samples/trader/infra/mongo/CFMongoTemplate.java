/*
 * Copyright (c) 2011. Gridshore
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

package org.axonframework.samples.trader.infra.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * CloudFoundry implementation of a MongoTemplate. We obtain the connection through the acquired factory.
 *
 * @author Jettro Coenradie
 */
public class CFMongoTemplate implements MongoTemplate {
    private static final String DEFAULT_DOMAINEVENTS_COLLECTION = "domainevents";
    private static final String DEFAULT_SNAPSHOTEVENTS_COLLECTION = "snapshotevents";

    private MongoDbFactory mongoDbFactory;

    public CFMongoTemplate(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
    }

    @Override
    public DBCollection domainEventCollection() {
        return database().getCollection(DEFAULT_DOMAINEVENTS_COLLECTION);
    }

    @Override
    public DBCollection snapshotEventCollection() {
        return database().getCollection(DEFAULT_SNAPSHOTEVENTS_COLLECTION);
    }

    @Override
    public DB database() {
        return mongoDbFactory.getDb();
    }
}
