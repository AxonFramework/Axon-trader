/*
 * Copyright (c) 2010. Gridshore
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

package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.StringAggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.samples.trader.app.api.company.CreateCompanyCommand;
import org.axonframework.samples.trader.app.api.order.CreateOrderBookCommand;
import org.axonframework.samples.trader.app.api.portfolio.money.AddMoneyToPortfolioCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.query.company.CompanyEntry;
import org.axonframework.samples.trader.app.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.app.query.orderbook.OrderEntry;
import org.axonframework.samples.trader.app.query.portfolio.PortfolioEntry;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>Initializes the repository with a number of users, companiess and order books</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class DBInit {

    private CommandBus commandBus;
    private CompanyQueryRepository companyRepository;
    private org.axonframework.eventstore.mongo.MongoTemplate systemAxonMongo;
    private MongoEventStore eventStore;
    private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Autowired
    public DBInit(CommandBus commandBus,
                  CompanyQueryRepository companyRepository,
                  org.axonframework.eventstore.mongo.MongoTemplate systemMongo,
                  MongoEventStore eventStore,
                  org.springframework.data.mongodb.core.MongoTemplate mongoTemplate) {
        this.commandBus = commandBus;
        this.companyRepository = companyRepository;
        this.systemAxonMongo = systemMongo;
        this.eventStore = eventStore;
        this.mongoTemplate = mongoTemplate;
    }

    public String obtainInfo() {
        Set<String> collectionNames = systemAxonMongo.database().getCollectionNames();
        StringBuilder sb = new StringBuilder();
        for (String name : collectionNames) {
            sb.append(name);
            sb.append("  ");
        }
        return sb.toString();
    }

    public void createItems() {
        systemAxonMongo.domainEventCollection().drop();
        systemAxonMongo.snapshotEventCollection().drop();
        mongoTemplate.dropCollection(UserEntry.class);
        mongoTemplate.dropCollection(OrderBookEntry.class);
        mongoTemplate.dropCollection(OrderEntry.class);
        mongoTemplate.dropCollection(CompanyEntry.class);
        mongoTemplate.dropCollection(TradeExecutedEntry.class);
        mongoTemplate.dropCollection(PortfolioEntry.class);
//        mongoTemplate.dropCollection(TransactionEntry.class);

        AggregateIdentifier userIdentifier = createuser("Buyer One", "buyer1");
        createuser("Buyer two", "buyer2");
        createuser("Buyer three", "buyer3");
        createuser("Admin One", "admin1");

        createCompanies(userIdentifier);
        createOrderBooks();
        eventStore.ensureIndexes();
    }

    public void addMoneyToPortfolio(String portfolioIdentifier, long amountOfMoney) {
        AddMoneyToPortfolioCommand command =
                new AddMoneyToPortfolioCommand(new UUIDAggregateIdentifier(portfolioIdentifier), amountOfMoney);
        commandBus.dispatch(command);
    }


    private void createCompanies(AggregateIdentifier userIdentifier) {
        CreateCompanyCommand command = new CreateCompanyCommand(userIdentifier, "Philips", 1000, 10000);
        commandBus.dispatch(command);

        command = new CreateCompanyCommand(userIdentifier, "Shell", 500, 5000);
        commandBus.dispatch(command);

        command = new CreateCompanyCommand(userIdentifier, "Bp", 15000, 100000);
        commandBus.dispatch(command);

//        To bo used for performance tests
//        for (int i=0; i < 1000; i++) {
//            command = new CreateCompanyCommand(userIdentifier, "Stock " + i, 15000, 100000);
//            commandBus.dispatch(command);
//        }

    }

    private void createOrderBooks() {
        Iterable<CompanyEntry> companyEntries = companyRepository.findAll();

        for (CompanyEntry companyEntry : companyEntries) {
            CreateOrderBookCommand command = new CreateOrderBookCommand(
                    new StringAggregateIdentifier(companyEntry.getIdentifier()));
            commandBus.dispatch(command, NoOpCallback.INSTANCE);
        }
    }

    private AggregateIdentifier createuser(String longName, String userName) {
        CreateUserCommand createUser = new CreateUserCommand(longName, userName, userName);
        FutureCallback<AggregateIdentifier> createUserCallback =
                new FutureCallback<AggregateIdentifier>();
        commandBus.dispatch(createUser, createUserCallback);
        AggregateIdentifier userIdentifier;
        try {
            userIdentifier = createUserCallback.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userIdentifier;
    }
}
