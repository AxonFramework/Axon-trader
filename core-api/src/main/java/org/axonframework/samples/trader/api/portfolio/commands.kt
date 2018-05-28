package org.axonframework.samples.trader.api.portfolio

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.axonframework.samples.trader.api.users.UserId

abstract class PortfolioCommand(@TargetAggregateIdentifier open val portfolioId: PortfolioId)

data class CreatePortfolioCommand(
        override val portfolioId: PortfolioId,
        val userId: UserId
) : PortfolioCommand(portfolioId)
