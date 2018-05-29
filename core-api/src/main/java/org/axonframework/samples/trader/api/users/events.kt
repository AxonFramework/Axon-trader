package org.axonframework.samples.trader.api.users

abstract class UserEvent(open val userId: UserId)

data class UserCreatedEvent(
        override val userId: UserId,
        val name: String,
        val username: String,
        val password: String
) : UserEvent(userId)

data class UserAuthenticatedEvent(override val userId: UserId) : UserEvent(userId)
