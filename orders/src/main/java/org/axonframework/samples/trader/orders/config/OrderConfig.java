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

package org.axonframework.samples.trader.orders.config;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.config.SagaConfiguration;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.orders.command.BuyTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Portfolio;
import org.axonframework.samples.trader.orders.command.SellTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean(name = "portfolioAggregateRepository")
    public Repository<Portfolio> portfolioAggregateRepository(AggregateFactory<Portfolio> portfolioAggregateFactory,
                                                              EventStore eventStore,
                                                              Cache cache,
                                                              SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return new CachingEventSourcingRepository<>(portfolioAggregateFactory,
                                                    eventStore,
                                                    cache,
                                                    snapshotTriggerDefinition);
    }

    @Bean(name = "transactionAggregateRepository")
    public Repository<Transaction> transactionAggregateRepository(
            AggregateFactory<Transaction> transactionAggregateFactory,
            EventStore eventStore,
            Cache cache,
            SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return new CachingEventSourcingRepository<>(transactionAggregateFactory,
                                                    eventStore,
                                                    cache,
                                                    snapshotTriggerDefinition);
    }


    @Bean
    public SagaConfiguration<BuyTradeManagerSaga> buyTradeSagaConfiguration() {
        return SagaConfiguration.trackingSagaManager(BuyTradeManagerSaga.class);
    }

    @Bean
    public SagaConfiguration<SellTradeManagerSaga> sellTradeSagaConfiguration() {
        return SagaConfiguration.trackingSagaManager(SellTradeManagerSaga.class);
    }
}
