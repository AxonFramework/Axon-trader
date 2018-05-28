package org.axonframework.samples.trader.api.portfolio.stock

import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.orders.transaction.TransactionId
import org.axonframework.samples.trader.api.portfolio.PortfolioEvent
import org.axonframework.samples.trader.api.portfolio.PortfolioId

data class ItemReservationCancelledForPortfolioEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfCancelledItems: Long
) : PortfolioEvent(portfolioId)

data class ItemReservationConfirmedForPortfolioEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfConfirmedItems: Long
) : PortfolioEvent(portfolioId)

data class ItemsAddedToPortfolioEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val amountOfItemsAdded: Long
) : PortfolioEvent(portfolioId)

data class ItemsReservedEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfItemsReserved: Long
) : PortfolioEvent(portfolioId)

data class ItemToReserveNotAvailableInPortfolioEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId
) : PortfolioEvent(portfolioId)

data class NotEnoughItemsAvailableToReserveInPortfolioEvent(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val availableAmountOfItems: Long,
        val amountOfItemsToReserve: Long
) : PortfolioEvent(portfolioId)
