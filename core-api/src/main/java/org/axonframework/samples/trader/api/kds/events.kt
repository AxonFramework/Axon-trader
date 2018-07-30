package org.axonframework.samples.trader.api.kds

abstract class StallEvent(open val stallId: StallId)

data class StallCreatedEvent(
    override val stallId: StallId,
    val poiId: Long,
    val stallName: String,
    val remark: String,
    val requirements: String,
    val ability: Int
) : StallEvent(stallId)

data class StallUpdatedEvent(
    val stallName: String,
    val remark: String,
    val requirements: String,
    val ability: Int
)

data class StallDeletedEvent(
    override val stallId: StallId
) : StallEvent(stallId)

