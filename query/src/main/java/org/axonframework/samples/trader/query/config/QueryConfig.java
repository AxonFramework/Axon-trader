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

package org.axonframework.samples.trader.query.config;

import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.SimpleEventHandlerInvoker;
import org.axonframework.eventhandling.SubscribingEventProcessor;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.samples.trader.query.company.CompanyEventHandler;
import org.axonframework.samples.trader.query.orderbook.OrderBookListener;
import org.axonframework.samples.trader.query.portfolio.PortfolioItemEventListener;
import org.axonframework.samples.trader.query.portfolio.PortfolioMoneyEventListener;
import org.axonframework.samples.trader.query.transaction.TransactionEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MongoDbConfiguration.class, HsqlDbConfiguration.class})
public class QueryConfig {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private CompanyEventHandler companyEventHandler;
    @Autowired
    private OrderBookListener orderBookListener;
    @Autowired
    private PortfolioItemEventListener portfolioItemEventListener;
    @Autowired
    private PortfolioMoneyEventListener portfolioMoneyEventListener;
    @Autowired
    private TransactionEventListener transactionEventListener;

    @Bean
    public EventProcessor queryEventProcessor() {
        SubscribingEventProcessor eventProcessor = new SubscribingEventProcessor("queryEventProcessor",
                new SimpleEventHandlerInvoker(
                        companyEventHandler,
                        orderBookListener,
                        portfolioItemEventListener,
                        portfolioMoneyEventListener,
                        transactionEventListener),
                eventStore);
        eventProcessor.start();

        return eventProcessor;
    }
}
