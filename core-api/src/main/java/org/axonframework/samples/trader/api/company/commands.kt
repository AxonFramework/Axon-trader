package org.axonframework.samples.trader.api.company

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.orders.OrderBookId
import org.axonframework.samples.trader.api.users.UserId

abstract class CompanyCommand(@TargetAggregateIdentifier open val companyId: CompanyId)

data class CreateCompanyCommand(
        override val companyId: CompanyId,
        val userId: UserId,
        val companyName: String,
        val companyValue: Long,
        val amountOfShares: Long
) : CompanyCommand(companyId)

data class AddOrderBookToCompanyCommand(
        override val companyId: CompanyId,
        val orderBookId: OrderBookId
) : CompanyCommand(companyId)
