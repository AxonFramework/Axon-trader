package org.axonframework.samples.trader.webui.init;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.mongo.MongoEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.mongo.MongoTemplate;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Profile("mongodb")
public class MongoDBInit extends BaseDBInit {
    private final static Logger logger = LoggerFactory.getLogger(MongoDBInit.class);

    private MongoTemplate systemAxonMongo;
    private MongoEventStorageEngine mongoEventStorageEngine;
    private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;
    private MongoTemplate systemAxonSagaMongo;
    private org.springframework.data.mongodb.core.MongoTemplate springTemplate;

    @Autowired
    public MongoDBInit(CommandBus commandBus,
                       CompanyQueryRepository companyRepository,
                       MongoTemplate systemMongo,
                       MongoEventStorageEngine mongoEventStorageEngine,
                       org.springframework.data.mongodb.core.MongoTemplate mongoTemplate,
                       MongoTemplate systemAxonSagaMongo,
                       PortfolioQueryRepository portfolioRepository,
                       OrderBookQueryRepository orderBookRepository, org.springframework.data.mongodb.core.MongoTemplate springTemplate) {
        super(commandBus, companyRepository, portfolioRepository, orderBookRepository);
        this.systemAxonMongo = systemMongo;
        this.mongoEventStorageEngine = mongoEventStorageEngine;
        this.mongoTemplate = mongoTemplate;
        this.systemAxonSagaMongo = systemAxonSagaMongo;
        this.springTemplate = springTemplate;
    }

    @Override
    public Set<String> obtainCollectionNames() {
        return springTemplate.getCollectionNames();
    }

    @Override
    public DataResults obtainCollection(String collectionName, int numItems, int start) {
        DBCursor dbCursor = springTemplate.getCollection(collectionName).find();
        List<DBObject> dbObjects = dbCursor.skip(start - 1).limit(numItems).toArray();

        List<Map> items = new ArrayList<>(dbCursor.length());
        for (DBObject dbObject : dbObjects) {
            items.add(dbObject.toMap());
        }

        int totalItems = dbCursor.count();

        return new DataResults(totalItems, items);
    }

    @Override
    public void createItemsIfNoUsersExist() {
        if (!mongoTemplate.collectionExists(UserEntry.class)) {
            createItems();
            logger.info("The database has been created and refreshed with some data.");
        }

    }

    @Override
    void initializeDB() {
        systemAxonMongo.eventCollection().drop();
        systemAxonMongo.snapshotCollection().drop();

//        systemAxonSagaMongo.sagaCollection.drop();

        mongoTemplate.dropCollection(UserEntry.class);
        mongoTemplate.dropCollection(OrderBookEntry.class);
        mongoTemplate.dropCollection(OrderEntry.class);
        mongoTemplate.dropCollection(CompanyEntry.class);
        mongoTemplate.dropCollection(TradeExecutedEntry.class);
        mongoTemplate.dropCollection(PortfolioEntry.class);
        mongoTemplate.dropCollection(TransactionEntry.class);
    }

    @Override
    void additionalDBSteps() {
        mongoEventStorageEngine.ensureIndexes();
    }
}
