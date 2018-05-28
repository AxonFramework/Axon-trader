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

import org.springframework.context.annotation.Configuration;

@Configuration
public class CompaniesConfig {

    //Specify snapshotter for Company Aggregate
//    private static final int SNAPSHOT_TRESHHOLD = 50;
//
//    @Autowired
//    private EventStore eventStore;
//
//    @Autowired
//    private Snapshotter snapshotter;
//
//    @Autowired
//    private Cache cache;
//
//    @Autowired
//    private CompanyOrderBookListener companyOrderBookListener;
//
//
//    @Bean(name = "companyRepository")
//    public Repository companyRepository(EventStore eventStore, Cache cache) {
//
//        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition = new EventCountSnapshotTriggerDefinition(
//                new SpringAggregateSnapshotter(),
//                SNAPSHOT_TRESHHOLD);
//
//        AggregateConfigurer<Company> companyAggregateConfigurer = AggregateConfigurer
//                .defaultConfiguration(Company.class);
//
//        CachingEventSourcingRepository<Company> repository = new CachingEventSourcingRepository<>(
//                companyAggregateConfigurer,
//                eventStore,
//                cache,
//                snapshotTriggerDefinition);
//
//        return companyAggregateConfigurer.configureRepository(conf -> repository);
//    }
//
//    @Bean
//    public Repository<Company> companyRepository() {
//        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition = new EventCountSnapshotTriggerDefinition(
//                snapshotter,
//                50);
//
//        CachingEventSourcingRepository<Company> repository = new CachingEventSourcingRepository<>(
//                companyAggregateFactory(),
//                eventStore,
//                cache,
//                snapshotTriggerDefinition);
//
//        return repository;
//    }
}
