package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.samples.trader.app.api.CreateOrderBookCommand;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.command.trading.TradeItemCommandHandler;
import org.axonframework.samples.trader.app.query.TradeItemEntry;
import org.axonframework.samples.trader.app.query.TradeItemRepository;
import org.axonframework.samples.trader.app.query.user.UserRepository;
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

    @Autowired
    public DBInit(CommandBus commandBus, TradeItemRepository tradeItemRepository) {
        this.commandBus = commandBus;
        this.tradeItemRepository = tradeItemRepository;
    }

    @PostConstruct
    public void createItems() {
        UUID userIdentifier = createuser("Buyer One", "buyer1");
        UUID adminIdentifier = createuser("Admin One", "admin1");

        createTradeItem(userIdentifier);
        createOrderBook();
    }

    private void createTradeItem(UUID userIdentifier) {
        CreateTradeItemCommand command = new CreateTradeItemCommand(
                userIdentifier, "Philips 3D TV",1000,10000);

        FutureCallback callback = new FutureCallback();
        commandBus.dispatch(command,callback);
        try {
            Object o = callback.get();
            System.out.println("toString " + o.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createOrderBook() {
        List<TradeItemEntry> tradeItemEntries = tradeItemRepository.listAllTradeItems();
        TradeItemEntry tradeItemEntry = tradeItemEntries.get(0);

        CreateOrderBookCommand command = new CreateOrderBookCommand(tradeItemEntry.getIdentifier());

        FutureCallback callback = new FutureCallback();
        commandBus.dispatch(command,callback);
        try {
            Object o = callback.get();
            System.out.println("toString " + o.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private UUID createuser(String username, String name) {
        CreateUserCommand createUser = new CreateUserCommand(username,name);
        FutureCallback createUserCallback = new FutureCallback();
        commandBus.dispatch(createUser,createUserCallback);
        UUID userIdentifier;
        try {
            userIdentifier = (UUID)createUserCallback.get();
            System.out.println("Identifier created user is : " + userIdentifier.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userIdentifier;
    }
}
