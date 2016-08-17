/*
 * Copyright (c) 2010-2016. Axon Framework
 *
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

package org.axonframework.samples.trader.infra.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.axonframework.eventsourcing.eventstore.mongo.DefaultMongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.net.UnknownHostException;

@Configuration
public class PersistenceInfrastructureConfig {

    @Bean
    @Profile("hsqldb")
    public EmbeddedDatabaseFactoryBean dataSource() {
        EmbeddedDatabaseFactoryBean embeddedDatabaseFactoryBean = new EmbeddedDatabaseFactoryBean();
        embeddedDatabaseFactoryBean.setDatabaseType(EmbeddedDatabaseType.HSQL);

        return embeddedDatabaseFactoryBean;
    }

    @Bean
    @Profile("mongodb")
    public Mongo mongo() throws UnknownHostException {
        return new MongoClient("127.0.0.1", 27017);
    }

    @Bean
    @Profile("mongodb")
    public MongoTemplate mongoSpringTemplate() throws UnknownHostException {
        return new MongoTemplate(mongo(), "axontrader");
    }

    @Bean
    @Profile("mongodb")
    public org.axonframework.eventsourcing.eventstore.mongo.MongoTemplate mongoTemplate() throws UnknownHostException {
        return new DefaultMongoTemplate(mongo(), "axontrader", "domainevents", "snapshotevents", null, null);
    }

    @Bean
    @Profile("mongodb")
    public org.axonframework.eventhandling.saga.repository.mongo.MongoTemplate mongoSagaTemplate()
            throws UnknownHostException {
        return new org.axonframework.eventhandling.saga.repository.mongo.DefaultMongoTemplate(mongo(),
                                                                                              "axontrader",
                                                                                              "snapshotevents",
                                                                                              null,
                                                                                              null);
    }
}