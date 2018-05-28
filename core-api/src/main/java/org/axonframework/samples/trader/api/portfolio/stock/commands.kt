package org.axonframework.samples.trader.api.portfolio.stock

import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.orders.transaction.TransactionId
import org.axonframework.samples.trader.api.portfolio.PortfolioCommand
import org.axonframework.samples.trader.api.portfolio.PortfolioId
import javax.validation.constraints.Min

data class AddItemsToPortfolioCommand(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        @Min(0) val amountOfItemsToAdd: Long
) : PortfolioCommand(portfolioId)

data class CancelItemReservationForPortfolioCommand(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfItemsToCancel: Long
) : PortfolioCommand(portfolioId)

data class ConfirmItemReservationForPortfolioCommand(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfItemsToConfirm: Long
) : PortfolioCommand(portfolioId)

data class ReserveItemsCommand(
        override val portfolioId: PortfolioId,
        val orderBookId: OrderBookId,
        val transactionId: TransactionId,
        val amountOfItemsToReserve: Long
) : PortfolioCommand(portfolioId)
