package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.samples.trader.app.api.CreateOrderBookCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Component
public class DBInit {

    private CommandBus commandBus;

    @Autowired
    public DBInit(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostConstruct
    public void createItems() {
        CreateOrderBookCommand command = new CreateOrderBookCommand(UUID.randomUUID());

        FutureCallback callback = new FutureCallback();
        commandBus.dispatch(command,callback);
        try {
            Object o = callback.get();
            System.out.println("toString " + o.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
