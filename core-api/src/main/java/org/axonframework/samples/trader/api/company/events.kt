package org.axonframework.samples.trader.api.company

import org.axonframework.samples.trader.api.orders.trades.OrderBookId

data class CompanyCreatedEvent(
        val companyId: CompanyId,
        val companyName: String,
        val companyValue: Long,
        val amountOfShares: Long
)

data class OrderBookAddedToCompanyEvent(
        val companyId: CompanyId,
        val orderBookId: OrderBookId
)
