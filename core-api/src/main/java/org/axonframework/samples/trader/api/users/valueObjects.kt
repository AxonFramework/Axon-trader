package org.axonframework.samples.trader.api.users

import org.axonframework.common.IdentifierFactory
import java.io.Serializable

data class UserId(val identifier: String = IdentifierFactory.getInstance().generateIdentifier()) : Serializable {

    companion object {
        private const val serialVersionUID = -4860092244272266543L
    }

}
