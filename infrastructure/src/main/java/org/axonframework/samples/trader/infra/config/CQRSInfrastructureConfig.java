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

import net.sf.ehcache.CacheManager;
import org.axonframework.cache.EhCacheAdapter;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventCountSnapshotterTrigger;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventsourcing.SpringAggregateSnapshotter;
import org.axonframework.eventstore.SnapshotEventStore;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;

@Configuration
@ComponentScan("org.axonframework.samples.trader")
@Import({CQRSInfrastructureHSQLDBConfig.class, CQRSInfrastructureMongoDBConfig.class})
public class CQRSInfrastructureConfig {

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.setDispatchInterceptors(Arrays.asList(new BeanValidationInterceptor()));

        return commandBus;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        return new AnnotationCommandHandlerBeanPostProcessor();
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        return new AnnotationEventListenerBeanPostProcessor();
    }

    @Bean
    public Snapshotter snapshotter(SnapshotEventStore eventStore) {
        SpringAggregateSnapshotter springAggregateSnapshotter = new SpringAggregateSnapshotter();
        springAggregateSnapshotter.setEventStore(eventStore);
        springAggregateSnapshotter.setExecutor(taskExecutor());

        return springAggregateSnapshotter;
    }

    @Bean
    public SnapshotterTrigger snapshotterTrigger(SnapshotEventStore eventStore) {
        EventCountSnapshotterTrigger eventCountSnapshotterTrigger = new EventCountSnapshotterTrigger();
        eventCountSnapshotterTrigger.setSnapshotter(snapshotter(eventStore));

        return eventCountSnapshotterTrigger;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        return threadPoolTaskExecutor;
    }

    @Bean
    public EhCacheAdapter ehCache(CacheManager cacheManager) {
        return new EhCacheAdapter(cacheManager.getCache("testCache"));
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setShared(true);

        return ehCacheManagerFactoryBean;
    }
}