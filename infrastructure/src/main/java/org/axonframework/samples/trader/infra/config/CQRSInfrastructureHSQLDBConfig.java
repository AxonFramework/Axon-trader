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

import org.axonframework.eventstore.jdbc.GenericEventSqlSchema;
import org.axonframework.eventstore.jdbc.JdbcEventStore;
import org.axonframework.saga.repository.jdbc.HsqlSagaSqlSchema;
import org.axonframework.saga.repository.jdbc.JdbcSagaRepository;
import org.axonframework.saga.repository.jdbc.SagaSqlSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("hsqldb")
public class CQRSInfrastructureHSQLDBConfig {

    @Bean
    public JdbcEventStore eventStore(DataSource dataSource) {
        return new JdbcEventStore(dataSource);
    }

    @Bean
    public GenericEventSqlSchema eventSqlSchema() {
        return new GenericEventSqlSchema();
    }

    @Bean
    public SagaSqlSchema sagaSqlSchema() {
        return new HsqlSagaSqlSchema();
    }

    @Bean
    public JdbcSagaRepository sagaRepository(DataSource dataSource) {
        return new JdbcSagaRepository(dataSource, sagaSqlSchema());
    }
}