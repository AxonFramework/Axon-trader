package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Component
public class TradeItemCommandHandler {
    private Repository<TradeItem> repository;

    @CommandHandler
    public void handleCreateTradeItem(CreateTradeItemCommand command) {
        TradeItem tradeItem = new TradeItem(UUID.randomUUID(),
                command.getTradeItemName(),
                command.getTradeItemValue(),
                command.getAmountOfShares());
        repository.add(tradeItem);
    }

    @Autowired
    public void setRepository(Repository<TradeItem> repository) {
        this.repository = repository;
    }

}
