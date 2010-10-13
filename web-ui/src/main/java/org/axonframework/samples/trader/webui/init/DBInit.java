package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.AggregateIdentifierFactory;
import org.axonframework.samples.trader.app.api.order.CreateOrderBookCommand;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemEntry;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>Initializes the repository with a number of users, trade items and order books</p>
 * @author Jettro Coenradie
 */
@Component
public class DBInit {

    private CommandBus commandBus;
    private TradeItemRepository tradeItemRepository;
    private MongoHelper mongo;
    private org.axonframework.eventstore.mongo.MongoHelper systemMongo;

    @Autowired
    public DBInit(CommandBus commandBus,
                  TradeItemRepository tradeItemRepository,
                  MongoHelper mongo,
                  org.axonframework.eventstore.mongo.MongoHelper systemMongo) {
        this.commandBus = commandBus;
        this.tradeItemRepository = tradeItemRepository;
        this.mongo = mongo;
        this.systemMongo = systemMongo;
    }

    @PostConstruct
    public void createItems() {
        mongo.getDatabase().dropDatabase();
        systemMongo.database().dropDatabase();

        AggregateIdentifier userIdentifier = createuser("Buyer One", "buyer1");
        createuser("Buyer two", "buyer2");
        createuser("Buyer three", "buyer3");
        createuser("Admin One", "admin1");

        createTradeItems(userIdentifier);
        createOrderBooks();
    }

    private void createTradeItems(AggregateIdentifier userIdentifier) {
        CreateTradeItemCommand command = new CreateTradeItemCommand(userIdentifier, "Philips", 1000, 10000);
        commandBus.dispatch(command);

        command = new CreateTradeItemCommand(userIdentifier, "Shell", 500, 5000);
        commandBus.dispatch(command);

        command = new CreateTradeItemCommand(userIdentifier, "Bp", 15000, 100000);
        commandBus.dispatch(command);

//        To bo used for performance tests
//        for (int i=0; i < 1000; i++) {
//            command = new CreateTradeItemCommand(userIdentifier, "Stock " + i, 15000, 100000);
//            commandBus.dispatch(command);
//        }

    }

    private void createOrderBooks() {
        List<TradeItemEntry> tradeItemEntries = tradeItemRepository.listAllTradeItems();

        for (TradeItemEntry tradeItemEntry : tradeItemEntries) {
            CreateOrderBookCommand command = new CreateOrderBookCommand(
                    AggregateIdentifierFactory.fromString(tradeItemEntry.getIdentifier()));
            commandBus.dispatch(command, NoOpCallback.INSTANCE);
        }
    }


    private AggregateIdentifier createuser(String longName, String userName) {
        CreateUserCommand createUser = new CreateUserCommand(longName, userName, userName);
        FutureCallback<CreateUserCommand, AggregateIdentifier> createUserCallback =
                new FutureCallback<CreateUserCommand, AggregateIdentifier>();
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
