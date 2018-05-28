package org.axonframework.samples.trader.api.portfolio.cash

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.transaction.TransactionId
import org.axonframework.samples.trader.api.portfolio.PortfolioId
import javax.validation.constraints.Min

abstract class PortfolioCommand(@TargetAggregateIdentifier open val portfolioId: PortfolioId)

data class CancelCashReservationCommand(
        override val portfolioId: PortfolioId,
        val transactionId: TransactionId,
        val amountOfMoneyToCancel: Long
) : PortfolioCommand(portfolioId)

data class ConfirmCashReservationCommand(
        override val portfolioId: PortfolioId,
        val transactionId: TransactionId,
        val amountOfMoneyToConfirmInCents: Long
) : PortfolioCommand(portfolioId)

data class DepositCashCommand(
        override val portfolioId: PortfolioId,
        @Min(0) val moneyToAddInCents: Long
) : PortfolioCommand(portfolioId)

data class ReserveCashCommand(
        override val portfolioId: PortfolioId,
        val transactionId: TransactionId,
        @Min(0) val amountOfMoneyToReserve: Long
) : PortfolioCommand(portfolioId)

data class WithdrawCashCommand(
        override val portfolioId: PortfolioId,
        @Min(0) val amountToPayInCents: Long
) : PortfolioCommand(portfolioId)
