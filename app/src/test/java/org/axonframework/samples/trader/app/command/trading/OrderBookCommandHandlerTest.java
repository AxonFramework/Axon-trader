package org.axonframework.samples.trader.app.command.trading;

import org.axonframework.samples.trader.app.api.order.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.*;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class OrderBookCommandHandlerTest {

    private FixtureConfiguration fixture;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        OrderBookCommandHandler commandHandler = new OrderBookCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(OrderBook.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testSimpleTradeExecution() {
        UUID buyOrder = UUID.randomUUID();
        UUID sellingUser = UUID.randomUUID();
        CreateSellOrderCommand orderCommand = new CreateSellOrderCommand(sellingUser,
                                                                         fixture.getAggregateIdentifier(),
                                                                         100,
                                                                         100);
        UUID sellOrder = orderCommand.getOrderId();
        fixture.given(new BuyOrderPlacedEvent(buyOrder, 200, 100, UUID.randomUUID()))
                .when(orderCommand)
                .expectEvents(new SellOrderPlacedEvent(sellOrder, 100, 100, sellingUser),
                              new TradeExecutedEvent(100, 100, buyOrder, sellOrder));
    }

    @Test
    public void testMassiveSellerTradeExecution() {
        UUID buyOrder1 = UUID.randomUUID();
        UUID buyOrder2 = UUID.randomUUID();
        UUID buyOrder3 = UUID.randomUUID();
        UUID sellingUser = UUID.randomUUID();
        CreateSellOrderCommand sellOrder = new CreateSellOrderCommand(sellingUser,
                                                                      fixture.getAggregateIdentifier(),
                                                                      200,
                                                                      100);
        UUID sellOrderId = sellOrder.getOrderId();
        fixture.given(new BuyOrderPlacedEvent(buyOrder1, 100, 100, UUID.randomUUID()),
                      new BuyOrderPlacedEvent(buyOrder2, 66, 120, UUID.randomUUID()),
                      new BuyOrderPlacedEvent(buyOrder3, 44, 140, UUID.randomUUID()))
                .when(sellOrder)
                .expectEvents(new SellOrderPlacedEvent(sellOrderId, 200, 100, sellingUser),
                              new TradeExecutedEvent(44, 120, buyOrder3, sellOrderId),
                              new TradeExecutedEvent(66, 110, buyOrder2, sellOrderId),
                              new TradeExecutedEvent(90, 100, buyOrder1, sellOrderId));
    }

    @Test
    public void testCreateOrderBook() {
        UUID orderBook = UUID.randomUUID();
        UUID tradeItemIdentifier = UUID.randomUUID();
        CreateOrderBookCommand createOrderBookCommand = new CreateOrderBookCommand(tradeItemIdentifier);
        fixture.given()
                .when(createOrderBookCommand)
                .expectEvents(new OrderBookCreatedEvent(tradeItemIdentifier));
    }
}
