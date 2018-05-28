package org.axonframework.samples.trader.api.orders.transaction

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.trades.OrderBookId
import org.axonframework.samples.trader.api.orders.trades.PortfolioId
import org.axonframework.samples.trader.api.orders.trades.TransactionId

abstract class AbstractStartTransactionCommand(
        @TargetAggregateIdentifier open val transactionId: TransactionId,
        open val orderBookId: OrderBookId,
        open val portfolioId: PortfolioId,
        open val tradeCount: Long,
        open val itemPrice: Long
)

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

data class CancelTransactionCommand(@TargetAggregateIdentifier val transactionId: TransactionId)

data class ConfirmTransactionCommand(@TargetAggregateIdentifier val transactionId: TransactionId)

data class ExecutedTransactionCommand(
        @TargetAggregateIdentifier val transactionId: TransactionId,
        val amountOfItems: Long,
        val itemPrice: Long
)
