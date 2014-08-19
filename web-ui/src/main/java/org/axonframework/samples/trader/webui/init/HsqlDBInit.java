package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.samples.trader.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initialisation of the Hsql datastore.
 */
@Component
@Profile("hsqldb")
public class HsqlDBInit extends BaseDBInit {
    private final static Logger logger = LoggerFactory.getLogger(HsqlDBInit.class);

    @Autowired
    public HsqlDBInit(CommandBus commandBus,
                      CompanyQueryRepository companyRepository,
                      PortfolioQueryRepository portfolioRepository,
                      OrderBookQueryRepository orderBookRepository) {
        super(commandBus, companyRepository, portfolioRepository, orderBookRepository);
    }

    @Override
    void initializeDB() {
        logger.debug("Initialize the hsqldb database.");
        // TODO jettro: Check and create schema, if exists empty the tables
    }

    @Override
    void additionalDBSteps() {
        logger.debug("Additional steps for the hsqldb database.");
        // TODO jettro: Check if we need to do something here
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
}
