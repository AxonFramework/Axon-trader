package org.axonframework.samples.trader.api.orders.trades

import org.axonframework.commandhandling.TargetAggregateIdentifier
import javax.validation.constraints.Min

abstract class AbstractOrderCommand(
        open val orderId: OrderId,
        open val portfolioId: PortfolioId,
        @TargetAggregateIdentifier open val orderBookId: OrderBookId,
        open val transactionId: TransactionId,
        @Min(0) open val tradeCount: Long,
        @Min(0) open val itemPrice: Long
)

data class CreateBuyOrderCommand(
        override val orderId: OrderId,
        override val portfolioId: PortfolioId,
        override val orderBookId: OrderBookId,
        override val transactionId: TransactionId,
        override val tradeCount: Long,
        override val itemPrice: Long
) : AbstractOrderCommand(orderId, portfolioId, orderBookId, transactionId, tradeCount, itemPrice)

data class CreateSellOrderCommand(
        override val orderId: OrderId,
        override val portfolioId: PortfolioId,
        override val orderBookId: OrderBookId,
        override val transactionId: TransactionId,
        override val tradeCount: Long,
        override val itemPrice: Long
) : AbstractOrderCommand(orderId, portfolioId, orderBookId, transactionId, tradeCount, itemPrice)

data class CreateOrderBookCommand(@TargetAggregateIdentifier val orderBookIdentifier: OrderBookId)
