package org.axonframework.samples.trader.api.company

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.trades.OrderBookId
import org.axonframework.samples.trader.api.users.UserId

data class CreateCompanyCommand(
        @TargetAggregateIdentifier val companyId: CompanyId,
        val userId: UserId,
        val companyName: String,
        val companyValue: Long,
        val amountOfShares: Long
)

data class AddOrderBookToCompanyCommand(
        @TargetAggregateIdentifier val companyId: CompanyId,
        val orderBookId: OrderBookId
)
