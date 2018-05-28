package org.axonframework.samples.trader.api.orders.transaction

import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.portfolio.PortfolioId

abstract class TransactionEvent(open val transactionId: TransactionId)

abstract class AbstractTransactionCancelledEvent(
        override val transactionId: TransactionId,
        open val totalAmountOfItems: Long,
        open val amountOfExecutedItems: Long
) : TransactionEvent(transactionId)

data class BuyTransactionCancelledEvent(
        override val transactionId: TransactionId,
        override val totalAmountOfItems: Long,
        override val amountOfExecutedItems: Long
) : AbstractTransactionCancelledEvent(transactionId, totalAmountOfItems, amountOfExecutedItems)

data class SellTransactionCancelledEvent(
        override val transactionId: TransactionId,
        override val totalAmountOfItems: Long,
        override val amountOfExecutedItems: Long
) : AbstractTransactionCancelledEvent(transactionId, totalAmountOfItems, amountOfExecutedItems)

abstract class AbstractTransactionConfirmedEvent(
        override val transactionId: TransactionId
) : TransactionEvent(transactionId)

data class BuyTransactionConfirmedEvent(
        override val transactionId: TransactionId
) : AbstractTransactionConfirmedEvent(transactionId)

data class SellTransactionConfirmedEvent(
        override val transactionId: TransactionId
) : AbstractTransactionConfirmedEvent(transactionId)


abstract class AbstractTransactionExecutedEvent(
        override val transactionId: TransactionId,
        open val amountOfItems: Long,
        open val itemPrice: Long
) : TransactionEvent(transactionId)

data class BuyTransactionExecutedEvent(
        override val transactionId: TransactionId,
        override val amountOfItems: Long,
        override val itemPrice: Long
) : AbstractTransactionExecutedEvent(transactionId, amountOfItems, itemPrice)

data class SellTransactionExecutedEvent(
        override val transactionId: TransactionId,
        override val amountOfItems: Long,
        override val itemPrice: Long
) : AbstractTransactionExecutedEvent(transactionId, amountOfItems, itemPrice)

abstract class AbstractTransactionPartiallyExecutedEvent(
        override val transactionId: TransactionId,
        open val amountOfExecutedItems: Long,
        open val totalOfExecutedItems: Long,
        open val itemPrice: Long
) : TransactionEvent(transactionId)

data class BuyTransactionPartiallyExecutedEvent(
        override val transactionId: TransactionId,
        override val amountOfExecutedItems: Long,
        override val totalOfExecutedItems: Long,
        override val itemPrice: Long
) : AbstractTransactionPartiallyExecutedEvent(transactionId, amountOfExecutedItems, totalOfExecutedItems, itemPrice)

data class SellTransactionPartiallyExecutedEvent(
        override val transactionId: TransactionId,
        override val amountOfExecutedItems: Long,
        override val totalOfExecutedItems: Long,
        override val itemPrice: Long
) : AbstractTransactionPartiallyExecutedEvent(transactionId, amountOfExecutedItems, totalOfExecutedItems, itemPrice)


abstract class AbstractTransactionStartedEvent(
        override val transactionId: TransactionId,
        open val orderBookId: OrderBookId,
        open val portfolioId: PortfolioId,
        open val totalItems: Long,
        open val pricePerItem: Long
) : TransactionEvent(transactionId)

data class BuyTransactionStartedEvent(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val totalItems: Long,
        override val pricePerItem: Long
) : AbstractTransactionStartedEvent(transactionId, orderBookId, portfolioId, totalItems, pricePerItem)

data class SellTransactionStartedEvent(
        override val transactionId: TransactionId,
        override val orderBookId: OrderBookId,
        override val portfolioId: PortfolioId,
        override val totalItems: Long,
        override val pricePerItem: Long
) : AbstractTransactionStartedEvent(transactionId, orderBookId, portfolioId, totalItems, pricePerItem)
