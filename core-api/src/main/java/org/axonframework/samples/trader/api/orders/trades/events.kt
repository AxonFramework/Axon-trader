package org.axonframework.samples.trader.api.orders.trades

import java.io.Serializable

abstract class AbstractOrderPlacedEvent(
        open val orderBookId: OrderBookId,
        open val orderId: OrderId,
        open val transactionId: TransactionId,
        open val tradeCount: Long,
        open val itemPrice: Long,
        open val portfolioId: PortfolioId
)

data class BuyOrderPlacedEvent(
        override val orderBookId: OrderBookId,
        override val orderId: OrderId,
        override val transactionId: TransactionId,
        override val tradeCount: Long,
        override val itemPrice: Long,
        override val portfolioId: PortfolioId
) : AbstractOrderPlacedEvent(orderBookId, orderId, transactionId, tradeCount, itemPrice, portfolioId)

data class SellOrderPlacedEvent(
        override val orderBookId: OrderBookId,
        override val orderId: OrderId,
        override val transactionId: TransactionId,
        override val tradeCount: Long,
        override val itemPrice: Long,
        override val portfolioId: PortfolioId
) : AbstractOrderPlacedEvent(orderBookId, orderId, transactionId, tradeCount, itemPrice, portfolioId)

data class OrderBookCreatedEvent(val orderBookId: OrderBookId)

data class TradeExecutedEvent(
        val orderBookIdentifier: OrderBookId,
        val tradeCount: Long,
        val tradePrice: Long,
        val buyOrderId: OrderId,
        val sellOrderId: OrderId,
        val buyTransactionId: TransactionId,
        val sellTransactionId: TransactionId
) : Serializable {
    companion object {
        private const val serialVersionUID = 6292249351659536792L
    }
}
