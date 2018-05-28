package org.axonframework.samples.trader.api.company

import org.axonframework.common.IdentifierFactory
import java.io.Serializable

class CompanyId(
        val identifier: String = IdentifierFactory.getInstance().generateIdentifier(),
        val hashCode: Int = identifier.hashCode()
) : Serializable {
    companion object {
        private const val serialVersionUID = -2521069615900157076L
    }
}
