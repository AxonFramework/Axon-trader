package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.AggregateIdentifierFactory;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.tradeitem.CreateTradeItemCommand;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        TradeItem tradeItem = new TradeItem(AggregateIdentifierFactory.randomIdentifier(),
                command.getTradeItemName(),
                command.getTradeItemValue(),
                command.getAmountOfShares());
        repository.add(tradeItem);

    }

    @Autowired
    @Qualifier("tradeItemRepository")
    public void setRepository(Repository<TradeItem> tradeItemRepository) {
        this.repository = tradeItemRepository;
    }

}
