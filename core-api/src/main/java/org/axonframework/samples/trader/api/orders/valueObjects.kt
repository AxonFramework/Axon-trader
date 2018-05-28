package org.axonframework.samples.trader.api.orders

import org.axonframework.common.IdentifierFactory
import java.io.Serializable

enum class TransactionType {
    SELL, BUY
}

data class OrderBookId(val identifier: String = IdentifierFactory.getInstance().generateIdentifier()) : Serializable {

    companion object {
        private const val serialVersionUID = -7842002574176005113L
    }

}

data class OrderId(val identifier: String = IdentifierFactory.getInstance().generateIdentifier()) : Serializable {

    companion object {
        private const val serialVersionUID = 4034328048230397374L
    }

}
