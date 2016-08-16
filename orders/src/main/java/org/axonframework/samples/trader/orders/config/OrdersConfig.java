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

import org.axonframework.cache.Cache;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotterTrigger;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.SpringPrototypeAggregateFactory;
import org.axonframework.eventstore.EventStore;
import org.axonframework.repository.Repository;
import org.axonframework.saga.GenericSagaFactory;
import org.axonframework.saga.ResourceInjector;
import org.axonframework.saga.SagaManager;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.annotation.AnnotatedSagaManager;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.samples.trader.orders.command.BuyTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Portfolio;
import org.axonframework.samples.trader.orders.command.SellTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Transaction;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OrdersConfig {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private EventStore eventStore;

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private Snapshotter snapshotter;

    @Autowired
    private Cache cache;

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        return new AnnotationAwareAspectJAutoProxyCreator();
    }

    @Bean
    @Scope("prototype")
    public Portfolio portfolio() {
        return new Portfolio();
    }

    @Bean
    @Scope("prototype")
    public Transaction transaction() {
        return new Transaction();
    }

    @Bean
    public AggregateFactory<Portfolio> portfolioAggregateFactory() {
        SpringPrototypeAggregateFactory<Portfolio> aggregateFactory = new SpringPrototypeAggregateFactory<>();
        aggregateFactory.setPrototypeBeanName("portfolio");

        return aggregateFactory;
    }

    @Bean
    public AggregateFactory<Transaction> transactionAggregateFactory() {
        SpringPrototypeAggregateFactory<Transaction> aggregateFactory = new SpringPrototypeAggregateFactory<>();
        aggregateFactory.setPrototypeBeanName("transaction");

        return aggregateFactory;
    }

    @Bean
    public Repository<Portfolio> portfolioRepository() {
        CachingEventSourcingRepository<Portfolio> repository = new CachingEventSourcingRepository<>(
                portfolioAggregateFactory(),
                eventStore);

        EventCountSnapshotterTrigger snapshotterTrigger = new EventCountSnapshotterTrigger();
        snapshotterTrigger.setTrigger(50);
        snapshotterTrigger.setSnapshotter(snapshotter);

        repository.setSnapshotterTrigger(snapshotterTrigger);
        repository.setEventBus(eventBus);
        repository.setCache(cache);

        return repository;
    }

    @Bean
    public Repository<Transaction> transactionRepository() {
        CachingEventSourcingRepository<Transaction> repository = new CachingEventSourcingRepository<>(
                transactionAggregateFactory(),
                eventStore);

        EventCountSnapshotterTrigger snapshotterTrigger = new EventCountSnapshotterTrigger();
        snapshotterTrigger.setTrigger(50);
        snapshotterTrigger.setSnapshotter(snapshotter);

        repository.setSnapshotterTrigger(snapshotterTrigger);
        repository.setEventBus(eventBus);
        repository.setCache(cache);

        return repository;
    }

    @Bean
    public ResourceInjector resourceInjector() {
        return new SpringResourceInjector();
    }

    @Bean
    public SagaManager sagaManager() {
        GenericSagaFactory genericSagaFactory = new GenericSagaFactory();
        genericSagaFactory.setResourceInjector(resourceInjector());

        AnnotatedSagaManager annotatedSagaManager = new AnnotatedSagaManager(sagaRepository,
                                                                             genericSagaFactory,
                                                                             BuyTradeManagerSaga.class,
                                                                             SellTradeManagerSaga.class);
        eventBus.subscribe(annotatedSagaManager);

        return annotatedSagaManager;
    }
}