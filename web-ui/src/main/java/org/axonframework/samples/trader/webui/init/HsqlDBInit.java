package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.saga.repository.jdbc.SagaSqlSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.EventTableFactory;
import org.axonframework.samples.trader.query.company.repositories.CompanyViewRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioViewRepository;
import org.axonframework.samples.trader.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.axonframework.samples.trader.query.transaction.repositories.TransactionViewRepository;
import org.axonframework.samples.trader.query.users.UserCommandHandler;
import org.axonframework.samples.trader.query.users.repositories.UserViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Initialisation of the Hsql datastore.
 */
@Component
@Profile("hsqldb")
public class HsqlDBInit extends BaseDBInit {

    private final static Logger logger = LoggerFactory.getLogger(HsqlDBInit.class);

    private EventTableFactory eventTableFactory;
    private EventSchema eventSchema;
    private SagaSqlSchema sagaSqlSchema;
    private DataSource dataSource;
    private UserViewRepository userViewRepository;
    private CompanyViewRepository companyViewRepository;
    private OrderBookViewRepository orderBookViewRepository;
    private PortfolioViewRepository portfolioViewRepository;
    private TradeExecutedQueryRepository tradeExecutedQueryRepository;
    private TransactionViewRepository transactionViewRepository;
    private UserCommandHandler userCommandHandler;

    @Autowired
    public HsqlDBInit(CommandBus commandBus,
                      CompanyViewRepository companyRepository,
                      PortfolioViewRepository portfolioRepository,
                      OrderBookViewRepository orderBookRepository,
                      EventTableFactory eventTableFactory,
                      EventSchema eventSchema,
                      SagaSqlSchema sagaSqlSchema, DataSource dataSource,
                      UserViewRepository userViewRepository, CompanyViewRepository companyViewRepository,
                      OrderBookViewRepository orderBookViewRepository,
                      PortfolioViewRepository portfolioViewRepository,
                      TradeExecutedQueryRepository tradeExecutedQueryRepository,
                      TransactionViewRepository transactionViewRepository,
                      UserCommandHandler userCommandHandler) {
        super(commandBus, userViewRepository, companyRepository, portfolioRepository, orderBookRepository, userCommandHandler);
        this.eventTableFactory = eventTableFactory;
        this.eventSchema = eventSchema;
        this.sagaSqlSchema = sagaSqlSchema;
        this.dataSource = dataSource;
        this.userViewRepository = userViewRepository;
        this.companyViewRepository = companyViewRepository;
        this.orderBookViewRepository = orderBookViewRepository;
        this.portfolioViewRepository = portfolioViewRepository;
        this.tradeExecutedQueryRepository = tradeExecutedQueryRepository;
        this.transactionViewRepository = transactionViewRepository;
        this.userCommandHandler = userCommandHandler;
    }

    @Override
    void initializeDB() {
        logger.debug("Initialize the hsqldb database.");
        // TODO jettro: Check and create schema, if exists empty the tables
        try {
            Connection connection = dataSource.getConnection();

            sql_dropDomainEventEntryTable(connection).execute();
            sql_dropSnapshotEventEntryTable(connection).execute();
            sql_dropTableAssocValueEntry(connection).execute();
            sql_dropTableSagaEntry(connection).execute();

            userViewRepository.deleteAll();
            transactionViewRepository.deleteAll();
            tradeExecutedQueryRepository.deleteAll();
            orderBookViewRepository.deleteAll();
            portfolioViewRepository.deleteAll();
            companyViewRepository.deleteAll();

            connection.commit();

            eventTableFactory.createDomainEventTable(connection, eventSchema)
                             .execute();
            eventTableFactory.createSnapshotEventTable(connection, eventSchema)
                             .execute();
            sagaSqlSchema.sql_createTableSagaEntry(connection).execute();
            sagaSqlSchema.sql_createTableAssocValueEntry(connection).execute();

            connection.commit();

            connection.close();
        } catch (SQLException e) {
            logger.error("Exception during database initialisation.", e);
        }
    }

    @Override
    void additionalDBSteps() {
        logger.debug("Additional steps for the hsqldb database.");
    }

    @Override
    public Set<String> obtainCollectionNames() {
        // TODO jettro: Implement this
        logger.debug("Obtain collections from hsqldb.");
        return null;
    }

    @Override
    public DataResults obtainCollection(String collectionName, int numItems, int start) {
        // TODO jettro: Implement this
        logger.debug("Obtain data for collection {}, num items {}, start from {}", collectionName, numItems, start);
        return null;
    }

    @Override
    public void createItemsIfNoUsersExist() {
        logger.info("Check if data needs to be initialized.");
        if (userViewRepository.count() == 0) {
            logger.info("Initializing the users.");
            createItems();
        }
    }

    public PreparedStatement sql_dropSnapshotEventEntryTable(Connection connection) throws SQLException {
        return connection.prepareStatement("drop table SNAPSHOTEVENTENTRY if exists;");
    }

    public PreparedStatement sql_dropDomainEventEntryTable(Connection connection) throws SQLException {
        return connection.prepareStatement("drop table DOMAINEVENTENTRY if exists;");
    }

    public PreparedStatement sql_dropTableAssocValueEntry(Connection conn) throws SQLException {
        return conn.prepareStatement("drop table ASSOCIATIONVALUEENTRY if exists;");
    }

    public PreparedStatement sql_dropTableSagaEntry(Connection conn) throws SQLException {
        return conn.prepareStatement("drop table SAGAENTRY if exists;");
    }
}
