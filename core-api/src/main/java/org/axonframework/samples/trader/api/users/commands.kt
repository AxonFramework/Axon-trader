package org.axonframework.samples.trader.api.users

import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

abstract class UserCommand(@TargetAggregateIdentifier open val userId: UserId)

class CreateUserCommand(
        override val userId: UserId,
        val name: String, @NotNull @Size(min = 3)
        val username: String, @NotNull @Size(min = 3)
        val password: String
) : UserCommand(userId)

data class AuthenticateUserCommand(
        override val userId: UserId,
        val userName: String,
        @NotNull @Size(min = 3) val password: CharArray
) : UserCommand(userId) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AuthenticateUserCommand) return false

        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (!Arrays.equals(password, other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + Arrays.hashCode(password)
        return result
    }

}
