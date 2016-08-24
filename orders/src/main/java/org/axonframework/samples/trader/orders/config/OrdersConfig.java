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
import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.SimpleEventHandlerInvoker;
import org.axonframework.eventhandling.SubscribingEventProcessor;
import org.axonframework.eventhandling.saga.AbstractSagaManager;
import org.axonframework.eventhandling.saga.AnnotatedSagaManager;
import org.axonframework.eventhandling.saga.ResourceInjector;
import org.axonframework.eventhandling.saga.SagaRepository;
import org.axonframework.eventhandling.saga.repository.AnnotatedSagaRepository;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.orders.command.BuyTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Portfolio;
import org.axonframework.samples.trader.orders.command.PortfolioManagementUserListener;
import org.axonframework.samples.trader.orders.command.SellTradeManagerSaga;
import org.axonframework.samples.trader.orders.command.Transaction;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.axonframework.spring.saga.SpringResourceInjector;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OrdersConfig {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private SagaStore<Object> sagaStore;

    @Autowired
    private Snapshotter snapshotter;

    @Autowired
    private Cache cache;

    @Autowired
    private PortfolioManagementUserListener portfolioManagementUserListener;

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
        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition = new EventCountSnapshotTriggerDefinition(
                snapshotter,
                50);

        CachingEventSourcingRepository<Portfolio> repository = new CachingEventSourcingRepository<>(
                portfolioAggregateFactory(),
                eventStore,
                cache,
                snapshotTriggerDefinition);


        return repository;
    }

    @Bean
    public Repository<Transaction> transactionRepository() {
        EventCountSnapshotTriggerDefinition triggerDefinition = new EventCountSnapshotTriggerDefinition(snapshotter,
                                                                                                        50);
        CachingEventSourcingRepository<Transaction> repository = new CachingEventSourcingRepository<>(
                transactionAggregateFactory(),
                eventStore,
                cache,
                triggerDefinition);

        return repository;
    }

    @Bean
    public ResourceInjector resourceInjector() {
        return new SpringResourceInjector();
    }

    @Bean
    public AbstractSagaManager<BuyTradeManagerSaga> buyTradeSagaManager() {
        return new AnnotatedSagaManager<>(
                BuyTradeManagerSaga.class,
                buyTradeSagaRepository()
        );
    }

    @Bean
    public AbstractSagaManager<SellTradeManagerSaga> sellTradeSagaManager() {
        return new AnnotatedSagaManager<>(
                SellTradeManagerSaga.class,
                sellTradeSagaRepository()
        );
    }

    @Bean
    public SagaRepository<BuyTradeManagerSaga> buyTradeSagaRepository() {
        return new AnnotatedSagaRepository<>(BuyTradeManagerSaga.class, sagaStore, resourceInjector());
    }

    @Bean
    public SagaRepository<SellTradeManagerSaga> sellTradeSagaRepository() {
        return new AnnotatedSagaRepository<>(SellTradeManagerSaga.class, sagaStore, resourceInjector());
    }

    @Bean
    public EventProcessor buyTradeSagaEventProcessor() {
        SubscribingEventProcessor eventProcessor = new SubscribingEventProcessor(
                "buyTradeSagaEventProcessor",
                buyTradeSagaManager(),
                eventStore);
        eventProcessor.start();

        return eventProcessor;
    }

    @Bean
    public EventProcessor sellTradeSagaEventProcessor() {
        SubscribingEventProcessor eventProcessor = new SubscribingEventProcessor(
                "sellTradeSagaEventProcessor",
                sellTradeSagaManager(),
                eventStore);
        eventProcessor.start();

        return eventProcessor;
    }

    @Bean
    public EventProcessor ordersEventProcessor() {
        SubscribingEventProcessor eventProcessor = new SubscribingEventProcessor("ordersEventProcessor",
                                                                                 new SimpleEventHandlerInvoker(
                                                                                         portfolioManagementUserListener),
                                                                                 eventStore);
        eventProcessor.start();

        return eventProcessor;
    }
}