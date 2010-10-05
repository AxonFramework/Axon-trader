package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.AggregateIdentifierFactory;
import org.axonframework.repository.Repository;
import org.axonframework.samples.trader.app.api.order.CreateBuyOrderCommand;
import org.axonframework.samples.trader.app.api.order.CreateOrderBookCommand;
import org.axonframework.samples.trader.app.api.order.CreateSellOrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Allard Buijze
 */
@Component
public class OrderBookCommandHandler {

    private Repository<OrderBook> repository;

    @CommandHandler
    public void handleBuyOrder(CreateBuyOrderCommand command) {
        // TODO add validation here
        OrderBook orderBook = repository.load(command.getOrderBookId(), null);

        orderBook.addBuyOrder(command.getOrderId(),
                command.getTradeCount(),
                command.getItemPrice(),
                command.getUserId());
    }

    @CommandHandler
    public void handleSellOrder(CreateSellOrderCommand command) {
        // TODO add validation here
        OrderBook orderBook = repository.load(command.getOrderBookId(), null);
        orderBook.addSellOrder(command.getOrderId(),
                command.getTradeCount(),
                command.getItemPrice(),
                command.getUserId());
    }

    @CommandHandler
    public void handleCreateOrderBook(CreateOrderBookCommand command) {
        OrderBook orderBook = new OrderBook(AggregateIdentifierFactory.randomIdentifier(), command.getTradeItemIdentifier());
        repository.add(orderBook);
    }

    @Autowired
    @Qualifier("orderBookRepository")
    public void setRepository(Repository<OrderBook> orderBookRepository) {
        this.repository = orderBookRepository;
    }
}
