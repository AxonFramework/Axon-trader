package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.CreateBuyOrderCommand;
import org.axonframework.samples.trader.app.api.CreateSellOrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Allard Buijze
 */
@Component
public class OrderBookCommandHandler {

    private Repository<OrderBook> repository;

    @CommandHandler
    public void handleBuyOrder(CreateBuyOrderCommand command) {
        OrderBook orderBook = repository.load(command.getTradeItemId(), null);
        orderBook.addBuyOrder(command.getOrderId(),
                              command.getTradeCount(),
                              command.getItemPrice(),
                              command.getUserId());
    }

    @CommandHandler
    public void handleSellOrder(CreateSellOrderCommand command) {
        OrderBook orderBook = repository.load(command.getTradeItemId(), null);
        orderBook.addSellOrder(command.getOrderId(),
                               command.getTradeCount(),
                               command.getItemPrice(),
                               command.getUserId());
    }

    @Autowired
    public void setRepository(Repository<OrderBook> repository) {
        this.repository = repository;
    }
}
