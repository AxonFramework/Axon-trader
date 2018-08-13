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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.samples.trader.company.command.Company;
import org.axonframework.samples.trader.orders.command.Portfolio;
import org.axonframework.samples.trader.orders.command.Transaction;
import org.axonframework.samples.trader.tradeengine.command.OrderBook;
import org.axonframework.samples.trader.users.command.User;
import org.axonframework.spring.config.CommandHandlerSubscriber;
import org.axonframework.spring.config.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Configuration
@ComponentScan("org.axonframework.samples.trader")
@Import(CQRSInfrastructureHSQLDBConfig.class)
public class CQRSInfrastructureConfig {

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());

        return commandBus;
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        return new AnnotationCommandHandlerBeanPostProcessor();
    }

    @Bean
    public CommandHandlerSubscriber commandHandlerSubscriber() {
        return new CommandHandlerSubscriber();
    }

    @Bean
    public org.axonframework.config.Configuration configuration(CommandBus commandBus,
                                                                EventStore eventStore,
                                                                ApplicationContext applicationContext) {
        EventHandlingConfiguration queryModelConfiguration =
                new EventHandlingConfiguration().registerSubscribingEventProcessor("queryModel");
        EventHandlingConfiguration commandPublisherConfiguration =
                new EventHandlingConfiguration().registerSubscribingEventProcessor("commandPublishingEventHandlers");

        Map<String, Object> eventHandlingComponents = applicationContext.getBeansWithAnnotation(ProcessingGroup.class);

        eventHandlingComponents.forEach((key, value) -> {
            if (key.contains("Listener")) {
                commandPublisherConfiguration.registerEventHandler(conf -> value);
            } else {
                queryModelConfiguration.registerEventHandler(conf -> value);
            }
        });

        org.axonframework.config.Configuration configuration =
                DefaultConfigurer.defaultConfiguration()
                                 .configureCommandBus(conf -> commandBus)
                                 .configureEventStore(conf -> eventStore)
                                 .configureAggregate(User.class)
                                 .configureAggregate(Company.class)
                                 .configureAggregate(Portfolio.class)
                                 .configureAggregate(Transaction.class)
                                 .configureAggregate(OrderBook.class)
                                 .registerModule(queryModelConfiguration)
                                 .registerModule(commandPublisherConfiguration)
                                 .buildConfiguration();
        configuration.start();
        return configuration;
    }
}