package org.axonframework.samples.trader.api.company

import org.axonframework.samples.trader.api.orders.trades.OrderBookId
import org.axonframework.samples.trader.api.users.UserId

data class CreateCompanyCommand(val companyId: CompanyId, val userId: UserId, val companyName: String, val companyValue: Long, val amountOfShares: Long)
data class AddOrderBookToCompanyCommand(val companyId: CompanyId, val orderBookId: OrderBookId)
