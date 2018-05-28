package org.axonframework.samples.trader.api.portfolio

import org.axonframework.samples.trader.api.users.UserId

abstract class PortfolioEvent(open val portfolioId: PortfolioId)

class PortfolioCreatedEvent(
        override val portfolioId: PortfolioId,
        val userId: UserId
) : PortfolioEvent(portfolioId)
