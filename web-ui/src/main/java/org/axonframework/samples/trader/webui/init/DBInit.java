/*
 * Copyright (c) 2010-2012. Axon Framework
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

package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.StringAggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.saga.repository.mongo.MongoTemplate;
import org.axonframework.samples.trader.company.api.CreateCompanyCommand;
import org.axonframework.samples.trader.orders.api.portfolio.item.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.orders.api.portfolio.money.DepositMoneyToPortfolioCommand;
import org.axonframework.samples.trader.query.company.CompanyEntry;
import org.axonframework.samples.trader.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.query.orderbook.OrderEntry;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.portfolio.PortfolioEntry;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.axonframework.samples.trader.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.query.transaction.TransactionEntry;
import org.axonframework.samples.trader.query.users.UserEntry;
import org.axonframework.samples.trader.tradeengine.api.order.CreateOrderBookCommand;
import org.axonframework.samples.trader.users.api.CreateUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * <p>Initializes the repository with a number of users, companiess and order books</p>
 *
 * @author Jettro Coenradie
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class DBInit {

    private CommandBus commandBus;
    private CompanyQueryRepository companyRepository;
    private PortfolioQueryRepository portfolioRepository;
    private OrderBookQueryRepository orderBookRepository;
    private org.axonframework.eventstore.mongo.MongoTemplate systemAxonMongo;
    private MongoEventStore eventStore;
    private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;
    private MongoTemplate systemAxonSagaMongo;

    @Autowired
    public DBInit(CommandBus commandBus,
                  CompanyQueryRepository companyRepository,
                  org.axonframework.eventstore.mongo.MongoTemplate systemMongo,
                  MongoEventStore eventStore,
                  org.springframework.data.mongodb.core.MongoTemplate mongoTemplate,
                  MongoTemplate systemAxonSagaMongo,
                  PortfolioQueryRepository portfolioRepository,
                  OrderBookQueryRepository orderBookRepository) {
        this.commandBus = commandBus;
        this.companyRepository = companyRepository;
        this.systemAxonMongo = systemMongo;
        this.eventStore = eventStore;
        this.mongoTemplate = mongoTemplate;
        this.systemAxonSagaMongo = systemAxonSagaMongo;
        this.portfolioRepository = portfolioRepository;
        this.orderBookRepository = orderBookRepository;
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

        systemAxonSagaMongo.sagaCollection().drop();
        systemAxonSagaMongo.associationsCollection().drop();

        mongoTemplate.dropCollection(UserEntry.class);
        mongoTemplate.dropCollection(OrderBookEntry.class);
        mongoTemplate.dropCollection(OrderEntry.class);
        mongoTemplate.dropCollection(CompanyEntry.class);
        mongoTemplate.dropCollection(TradeExecutedEntry.class);
        mongoTemplate.dropCollection(PortfolioEntry.class);
        mongoTemplate.dropCollection(TransactionEntry.class);

        AggregateIdentifier buyer1 = createuser("Buyer One", "buyer1");
        AggregateIdentifier buyer2 = createuser("Buyer two", "buyer2");
        AggregateIdentifier buyer3 = createuser("Buyer three", "buyer3");
        AggregateIdentifier buyer4 = createuser("Buyer four", "buyer4");
        AggregateIdentifier buyer5 = createuser("Buyer four", "buyer5");
        AggregateIdentifier buyer6 = createuser("Buyer four", "buyer6");

        createCompanies(buyer1);
        createOrderBooks();
        addMoney(buyer1, 100000);
        addItems(buyer2, "Philips", 10000l);
        addMoney(buyer3, 100000);
        addItems(buyer4, "Shell", 10000l);
        addMoney(buyer5, 100000);
        addItems(buyer6, "Bp", 10000l);

        eventStore.ensureIndexes();
    }

    private void addItems(AggregateIdentifier user, String companyName, long amount) {
        PortfolioEntry portfolioEntry = portfolioRepository.findByUserIdentifier(user.asString());
        OrderBookEntry orderBookEntry = obtainOrderBookByCompanyName(companyName);
        AddItemsToPortfolioCommand command = new AddItemsToPortfolioCommand(
                new UUIDAggregateIdentifier(portfolioEntry.getIdentifier()),
                new UUIDAggregateIdentifier(orderBookEntry.getIdentifier()),
                amount);
        commandBus.dispatch(command);
    }

    private OrderBookEntry obtainOrderBookByCompanyName(String companyName) {
        Iterable<CompanyEntry> companyEntries = companyRepository.findAll();
        for (CompanyEntry entry : companyEntries) {
            if (entry.getName().equals(companyName)) {
                List<OrderBookEntry> orderBookEntries = orderBookRepository
                        .findByCompanyIdentifier(entry.getIdentifier());

                return orderBookEntries.get(0);
            }
        }
        throw new RuntimeException("Problem initializing, could not find company with required name.");
    }

    private void addMoney(AggregateIdentifier buyer1, long amount) {
        PortfolioEntry portfolioEntry = portfolioRepository.findByUserIdentifier(buyer1.asString());
        depositMoneyToPortfolio(portfolioEntry.getIdentifier(), amount);
    }

    public void depositMoneyToPortfolio(String portfolioIdentifier, long amountOfMoney) {
        DepositMoneyToPortfolioCommand command =
                new DepositMoneyToPortfolioCommand(new UUIDAggregateIdentifier(portfolioIdentifier), amountOfMoney);
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
            commandBus.dispatch(command);
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
