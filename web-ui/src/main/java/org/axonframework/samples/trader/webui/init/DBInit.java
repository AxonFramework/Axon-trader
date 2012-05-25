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
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.saga.repository.mongo.MongoTemplate;
import org.axonframework.samples.trader.company.api.CompanyId;
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
import org.axonframework.samples.trader.tradeengine.api.order.OrderBookId;
import org.axonframework.samples.trader.tradeengine.api.order.PortfolioId;
import org.axonframework.samples.trader.users.api.CreateUserCommand;
import org.axonframework.samples.trader.users.api.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

        UserId buyer1 = createuser("Buyer One", "buyer1");
        UserId buyer2 = createuser("Buyer two", "buyer2");
        UserId buyer3 = createuser("Buyer three", "buyer3");
        UserId buyer4 = createuser("Buyer four", "buyer4");
        UserId buyer5 = createuser("Buyer four", "buyer5");
        UserId buyer6 = createuser("Buyer four", "buyer6");

        createCompanies(buyer1);

        addMoney(buyer1, 100000);
        addItems(buyer2, "Philips", 10000l);
        addMoney(buyer3, 100000);
        addItems(buyer4, "Shell", 10000l);
        addMoney(buyer5, 100000);
        addItems(buyer6, "Bp", 10000l);

        eventStore.ensureIndexes();
    }

    private void addItems(UserId user, String companyName, long amount) {
        PortfolioEntry portfolioEntry = portfolioRepository.findByUserIdentifier(user.toString());
        OrderBookEntry orderBookEntry = obtainOrderBookByCompanyName(companyName);
        AddItemsToPortfolioCommand command = new AddItemsToPortfolioCommand(
                new PortfolioId(portfolioEntry.getIdentifier()),
                new OrderBookId(orderBookEntry.getIdentifier()),
                amount);
        commandBus.dispatch(new GenericCommandMessage<AddItemsToPortfolioCommand>(command));
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

    private void addMoney(UserId buyer1, long amount) {
        PortfolioEntry portfolioEntry = portfolioRepository.findByUserIdentifier(buyer1.toString());
        depositMoneyToPortfolio(portfolioEntry.getIdentifier(), amount);
    }

    public void depositMoneyToPortfolio(String portfolioIdentifier, long amountOfMoney) {
        DepositMoneyToPortfolioCommand command =
                new DepositMoneyToPortfolioCommand(new PortfolioId(portfolioIdentifier), amountOfMoney);
        commandBus.dispatch(new GenericCommandMessage<DepositMoneyToPortfolioCommand>(command));
    }


    private void createCompanies(UserId userIdentifier) {
        CreateCompanyCommand command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Philips", 1000, 10000);
        commandBus.dispatch(new GenericCommandMessage<CreateCompanyCommand>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Shell", 500, 5000);
        commandBus.dispatch(new GenericCommandMessage<CreateCompanyCommand>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Bp", 15000, 100000);
        commandBus.dispatch(new GenericCommandMessage<CreateCompanyCommand>(command));

//        To bo used for performance tests
//        for (int i=0; i < 1000; i++) {
//            command = new CreateCompanyCommand(userIdentifier, "Stock " + i, 15000, 100000);
//            commandBus.dispatch(command);
//        }

    }

    private UserId createuser(String longName, String userName) {
        UserId userId = new UserId();
        CreateUserCommand createUser = new CreateUserCommand(userId, longName, userName, userName);
        commandBus.dispatch(new GenericCommandMessage<CreateUserCommand>(createUser));
        return userId;
    }
}
