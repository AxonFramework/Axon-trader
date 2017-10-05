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

package org.axonframework.samples.trader.listener.config;

import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.SimpleEventHandlerInvoker;
import org.axonframework.eventhandling.SubscribingEventProcessor;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.listener.ExecutedTradesBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("org.axonframework.samples.trader.listener")
@PropertySource("classpath:external-config.properties")
public class ExternalListenersConfig {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private ExecutedTradesBroadcaster executedTradesBroadcaster;

    @Bean
    public EventProcessor externalListenersEventProcessor() {
        SubscribingEventProcessor eventProcessor = new SubscribingEventProcessor("externalListenersEventProcessor",
                new SimpleEventHandlerInvoker(executedTradesBroadcaster),
                eventStore);

        eventProcessor.start();

        return eventProcessor;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
}