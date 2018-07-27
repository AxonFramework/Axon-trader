package org.axonframework.samples.trader.api.kds

import org.axonframework.common.IdentifierFactory
import java.io.Serializable

data class StallId(val identifier: String = IdentifierFactory.getInstance().generateIdentifier()) : Serializable {

    companion object {
        private const val serialVersionUID = -2521069615900157076L
    }
}