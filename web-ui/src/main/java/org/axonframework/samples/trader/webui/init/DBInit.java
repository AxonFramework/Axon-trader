package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
import org.axonframework.samples.trader.app.api.CreateOrderBookCommand;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.axonframework.samples.trader.app.query.TradeItemEntry;
import org.axonframework.samples.trader.app.query.TradeItemRepository;
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
        mongo.users().drop();
        mongo.tradeItems().drop();

        UUID userIdentifier = createuser("Buyer One", "buyer1");
        createuser("Buyer two", "buyer2");
        createuser("Buyer three", "buyer3");
        createuser("Admin One", "admin1");

        createTradeItem(userIdentifier);
        createOrderBook();
    }

    private void createTradeItem(UUID userIdentifier) {
        CreateTradeItemCommand command = new CreateTradeItemCommand(
                userIdentifier, "Philips 3D TV", 1000, 10000);

        commandBus.dispatch(command, NoOpCallback.INSTANCE);
    }

    private void createOrderBook() {
        List<TradeItemEntry> tradeItemEntries = tradeItemRepository.listAllTradeItems();
        TradeItemEntry tradeItemEntry = tradeItemEntries.get(0);

        CreateOrderBookCommand command = new CreateOrderBookCommand(tradeItemEntry.getIdentifier());

        commandBus.dispatch(command, NoOpCallback.INSTANCE);
    }


    private UUID createuser(String username, String name) {
        CreateUserCommand createUser = new CreateUserCommand(username, name);
        FutureCallback<CreateUserCommand, UUID> createUserCallback = new FutureCallback<CreateUserCommand, UUID>();
        commandBus.dispatch(createUser, createUserCallback);
        UUID userIdentifier;
        try {
            userIdentifier = createUserCallback.get();
            System.out.println("Identifier created user is : " + userIdentifier.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userIdentifier;
    }
}
