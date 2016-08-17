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

package org.axonframework.samples.trader.tradeengine.config;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotterTrigger;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.tradeengine.command.OrderBook;
import org.axonframework.samples.trader.tradeengine.command.OrderBookCommandHandler;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TradeEngineConfig {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private Snapshotter snapshotter;

    @Autowired
    private Cache cache;

    @Bean
    @Scope("prototype")
    public OrderBook orderBook() {
        return new OrderBook();
    }

    @Bean
    public AggregateFactory<OrderBook> orderBookAggregateFactory() {
        SpringPrototypeAggregateFactory<OrderBook> aggregateFactory = new SpringPrototypeAggregateFactory<>();
        aggregateFactory.setPrototypeBeanName("orderBook");

        return aggregateFactory;
    }

    @Bean
    public OrderBookCommandHandler orderBookCommandHandler() {
        OrderBookCommandHandler orderBookCommandHandler = new OrderBookCommandHandler();
        orderBookCommandHandler.setRepository(orderBookRepository());

        return orderBookCommandHandler;
    }

    @Bean
    public Repository<OrderBook> orderBookRepository() {
        CachingEventSourcingRepository<OrderBook> repository = new CachingEventSourcingRepository<>(
                orderBookAggregateFactory(),
                eventStore,
                cache);

        EventCountSnapshotterTrigger eventCountSnapshotterTrigger = new EventCountSnapshotterTrigger();
        eventCountSnapshotterTrigger.setSnapshotter(snapshotter);
        eventCountSnapshotterTrigger.setTrigger(50);

        repository.setSnapshotterTrigger(eventCountSnapshotterTrigger);

        return repository;
    }
}
