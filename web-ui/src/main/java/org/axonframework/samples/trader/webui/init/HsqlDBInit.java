package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventstore.jdbc.EventSqlSchema;
import org.axonframework.saga.repository.jdbc.SagaSqlSchema;
import org.axonframework.samples.trader.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.axonframework.samples.trader.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.axonframework.samples.trader.query.transaction.repositories.TransactionQueryRepository;
import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
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

    private EventSqlSchema eventSqlSchema;
    private SagaSqlSchema sagaSqlSchema;
    private DataSource dataSource;
    private UserQueryRepository userQueryRepository;
    private CompanyQueryRepository companyQueryRepository;
    private OrderBookQueryRepository orderBookQueryRepository;
    private PortfolioQueryRepository portfolioQueryRepository;
    private TradeExecutedQueryRepository tradeExecutedQueryRepository;
    private TransactionQueryRepository transactionQueryRepository;

    @Autowired
    public HsqlDBInit(CommandBus commandBus,
                      CompanyQueryRepository companyRepository,
                      PortfolioQueryRepository portfolioRepository,
                      OrderBookQueryRepository orderBookRepository,
                      EventSqlSchema eventSqlSchema,
                      SagaSqlSchema sagaSqlSchema, DataSource dataSource,
                      UserQueryRepository userQueryRepository, CompanyQueryRepository companyQueryRepository, OrderBookQueryRepository orderBookQueryRepository, PortfolioQueryRepository portfolioQueryRepository, TradeExecutedQueryRepository tradeExecutedQueryRepository, TransactionQueryRepository transactionQueryRepository) {
        super(commandBus, companyRepository, portfolioRepository, orderBookRepository);
        this.eventSqlSchema = eventSqlSchema;
        this.sagaSqlSchema = sagaSqlSchema;
        this.dataSource = dataSource;
        this.userQueryRepository = userQueryRepository;
        this.companyQueryRepository = companyQueryRepository;
        this.orderBookQueryRepository = orderBookQueryRepository;
        this.portfolioQueryRepository = portfolioQueryRepository;
        this.tradeExecutedQueryRepository = tradeExecutedQueryRepository;
        this.transactionQueryRepository = transactionQueryRepository;
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

            userQueryRepository.deleteAll();
            transactionQueryRepository.deleteAll();
            tradeExecutedQueryRepository.deleteAll();
            orderBookQueryRepository.deleteAll();
            portfolioQueryRepository.deleteAll();
            companyQueryRepository.deleteAll();

            connection.commit();

            eventSqlSchema.sql_createDomainEventEntryTable(connection).execute();
            eventSqlSchema.sql_createSnapshotEventEntryTable(connection).execute();
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
        if (userQueryRepository.count() == 0) {
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
