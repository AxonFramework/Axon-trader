package org.axonframework.samples.trader.api.company

import org.axonframework.samples.trader.api.orders.OrderBookId

abstract class CompanyEvent(open val companyId: CompanyId)

data class CompanyCreatedEvent(
        override val companyId: CompanyId,
        val companyName: String,
        val companyValue: Long,
        val amountOfShares: Long
) : CompanyEvent(companyId)

data class OrderBookAddedToCompanyEvent(
        override val companyId: CompanyId,
        val orderBookId: OrderBookId
) : CompanyEvent(companyId)
