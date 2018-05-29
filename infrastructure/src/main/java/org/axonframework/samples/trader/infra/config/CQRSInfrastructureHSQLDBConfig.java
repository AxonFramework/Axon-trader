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

import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.jdbc.HsqlSagaSqlSchema;
import org.axonframework.eventhandling.saga.repository.jdbc.JdbcSagaStore;
import org.axonframework.eventhandling.saga.repository.jdbc.SagaSqlSchema;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.EventTableFactory;
import org.axonframework.eventsourcing.eventstore.jdbc.HsqlEventTableFactory;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.spring.jdbc.SpringDataSourceConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("hsqldb")
public class CQRSInfrastructureHSQLDBConfig {

    @Bean
    public SpringDataSourceConnectionProvider springDataSourceConnectionProvider(DataSource dataSource) {
        return new SpringDataSourceConnectionProvider(dataSource);
    }

    @Bean
    public JdbcEventStorageEngine eventStorageEngine(ConnectionProvider connectionProvider) {
        return new JdbcEventStorageEngine(connectionProvider, NoTransactionManager.INSTANCE);
    }
    @Bean
    public EventTableFactory eventSchemaFactory() {
        return HsqlEventTableFactory.INSTANCE;
    }

    @Bean
    public EventSchema eventSchema() {
        return new EventSchema();
    }

    @Bean
    public SagaSqlSchema sagaSqlSchema() {
        return new HsqlSagaSqlSchema();
    }

    @Bean
    public SagaStore<Object> sagaRepository(DataSource dataSource) {
        return new JdbcSagaStore(dataSource, sagaSqlSchema());
    }
}