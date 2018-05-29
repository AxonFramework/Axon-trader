package org.axonframework.samples.trader.api.orders.transaction

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.orders.TransactionType
import org.axonframework.samples.trader.api.portfolio.PortfolioId

abstract class TransactionCommand(@TargetAggregateIdentifier open val transactionId: TransactionId)

abstract class AbstractStartTransactionCommand(
        override val transactionId: TransactionId,
        open val orderBookId: OrderBookId,
        open val portfolioId: PortfolioId,
        open val tradeCount: Long,
        open val pricePerItem: Long
) : TransactionCommand(transactionId) {
    abstract val transactionType: TransactionType
}

data class StartBuyTransactionCommand(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val tradeCount: Long,
        override val pricePerItem: Long
) : AbstractStartTransactionCommand(transactionId, orderBookId, portfolioId, tradeCount, pricePerItem) {
    override val transactionType: TransactionType = TransactionType.BUY
}

data class StartSellTransactionCommand(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val tradeCount: Long,
        override val pricePerItem: Long
) : AbstractStartTransactionCommand(transactionId, orderBookId, portfolioId, tradeCount, pricePerItem) {
    override val transactionType: TransactionType = TransactionType.SELL
}

data class CancelTransactionCommand(override val transactionId: TransactionId) : TransactionCommand(transactionId)

data class ConfirmTransactionCommand(override val transactionId: TransactionId) : TransactionCommand(transactionId)

data class ExecutedTransactionCommand(
        override val transactionId: TransactionId,
        val amountOfItems: Long,
        val itemPrice: Long
) : TransactionCommand(transactionId)
