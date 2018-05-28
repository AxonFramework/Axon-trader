package org.axonframework.samples.trader.api.orders.transaction

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.portfolio.PortfolioId

abstract class TransactionCommand(@TargetAggregateIdentifier open val transactionId: TransactionId)

abstract class AbstractStartTransactionCommand(
        override val transactionId: TransactionId,
        open val orderBookId: OrderBookId,
        open val portfolioId: PortfolioId,
        open val tradeCount: Long,
        open val itemPrice: Long
) : TransactionCommand(transactionId)

data class StartBuyTransactionCommand(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val tradeCount: Long,
        override val itemPrice: Long
) : AbstractStartTransactionCommand(transactionId, orderBookId, portfolioId, tradeCount, itemPrice)

data class StartSellTransactionCommand(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val tradeCount: Long,
        override val itemPrice: Long
) : AbstractStartTransactionCommand(transactionId, orderBookId, portfolioId, tradeCount, itemPrice)

data class CancelTransactionCommand(override val transactionId: TransactionId) : TransactionCommand(transactionId)

data class ConfirmTransactionCommand(override val transactionId: TransactionId) : TransactionCommand(transactionId)

data class ExecutedTransactionCommand(
        override val transactionId: TransactionId,
        val amountOfItems: Long,
        val itemPrice: Long
) : TransactionCommand(transactionId)
