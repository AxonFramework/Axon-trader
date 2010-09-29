package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
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
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Component
public class DBInit {

    private CommandBus commandBus;
    private TradeItemRepository tradeItemRepository;
    private MongoHelper mongo;

    @Autowired
    public DBInit(CommandBus commandBus,
                  TradeItemRepository tradeItemRepository,
                  MongoHelper mongo) {
        this.commandBus = commandBus;
        this.tradeItemRepository = tradeItemRepository;
        this.mongo = mongo;
    }

    @PostConstruct
    public void createItems() {
        mongo.getDatabase().dropDatabase();
        mongo.systemDatabase().dropDatabase();

        UUID userIdentifier = createuser("Buyer One", "buyer1");
        createuser("Buyer two", "buyer2");
        createuser("Buyer three", "buyer3");
        createuser("Admin One", "admin1");

        createTradeItems(userIdentifier);
        createOrderBooks();
    }

    private void createTradeItems(UUID userIdentifier) {
        CreateTradeItemCommand command = new CreateTradeItemCommand(userIdentifier, "Philips 3D TV", 1000, 10000);
        commandBus.dispatch(command, NoOpCallback.INSTANCE);

        command = new CreateTradeItemCommand(userIdentifier,"Sony Ultra TV", 500, 5000);
        commandBus.dispatch(command, NoOpCallback.INSTANCE);

        command = new CreateTradeItemCommand(userIdentifier,"Smart 2 wheel roller", 15000, 100000);
        commandBus.dispatch(command, NoOpCallback.INSTANCE);

    }

    private void createOrderBooks() {
        List<TradeItemEntry> tradeItemEntries = tradeItemRepository.listAllTradeItems();

        for (TradeItemEntry tradeItemEntry : tradeItemEntries) {
            CreateOrderBookCommand command = new CreateOrderBookCommand(tradeItemEntry.getIdentifier());
            commandBus.dispatch(command, NoOpCallback.INSTANCE);
        }
    }


    private UUID createuser(String longName, String userName) {
        CreateUserCommand createUser = new CreateUserCommand(longName, userName, userName);
        FutureCallback<CreateUserCommand, UUID> createUserCallback = new FutureCallback<CreateUserCommand, UUID>();
        commandBus.dispatch(createUser, createUserCallback);
        UUID userIdentifier;
        try {
            userIdentifier = createUserCallback.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userIdentifier;
    }
}
