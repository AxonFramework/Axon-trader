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

package org.axonframework.samples.trader.company.config;

import org.axonframework.cache.Cache;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotterTrigger;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.SpringPrototypeAggregateFactory;
import org.axonframework.eventstore.EventStore;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.company.command.Company;
import org.axonframework.samples.trader.company.command.CompanyCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CompaniesConfig {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private EventStore eventStore;

    @Autowired
    private Snapshotter snapshotter;

    @Autowired
    private Cache cache;

    @Bean
    @Scope("prototype")
    public Company company() {
        return new Company();
    }

    @Bean
    public Repository<Company> companyRepository() {
        CachingEventSourcingRepository<Company> repository = new CachingEventSourcingRepository<>(
                companyAggregateFactory(),
                eventStore);

        EventCountSnapshotterTrigger eventCountSnapshotterTrigger = new EventCountSnapshotterTrigger();
        eventCountSnapshotterTrigger.setSnapshotter(snapshotter);
        eventCountSnapshotterTrigger.setTrigger(50);

        repository.setSnapshotterTrigger(eventCountSnapshotterTrigger);
        repository.setEventBus(eventBus);
        repository.setCache(cache);

        return repository;
    }

    @Bean
    public CompanyCommandHandler companyCommandHandler() {
        CompanyCommandHandler companyCommandHandler = new CompanyCommandHandler();
        companyCommandHandler.setRepository(companyRepository());

        return companyCommandHandler;
    }

    @Bean
    public AggregateFactory<Company> companyAggregateFactory() {
        SpringPrototypeAggregateFactory springPrototypeAggregateFactory = new SpringPrototypeAggregateFactory();
        springPrototypeAggregateFactory.setPrototypeBeanName("company");

        return springPrototypeAggregateFactory;
    }
}