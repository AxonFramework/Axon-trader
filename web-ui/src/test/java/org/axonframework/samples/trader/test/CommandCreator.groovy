/*
 * Copyright (c) 2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.test

import org.axonframework.samples.trader.orders.api.transaction.StartBuyTransactionCommand
import org.axonframework.samples.trader.orders.api.transaction.StartSellTransactionCommand
import org.axonframework.samples.trader.query.portfolio.PortfolioEntry
import org.axonframework.samples.trader.tradeengine.api.order.OrderBookId
import org.axonframework.samples.trader.tradeengine.api.order.PortfolioId

/**
 * Class used to create an order based on the provided Profile. If the profile has money we place buy orders, if
 * the profile is almost out of money we start selling stuff.
 *
 * @author Jettro Coenradie
 */
class CommandCreator {
    Random randomFactory = new Random()
    def orderBookEntries
    def command

    def CommandCreator(orderBookEntries) {
        this.orderBookEntries = orderBookEntries
    }

    def createCommand(PortfolioEntry portfolio) {
        if (portfolio.amountOfMoney - portfolio.reservedAmountOfMoney > 10000) {
            command = new StartBuyTransactionCommand(
                    new OrderBookId(obtainRandomOrderBook()),
                    new PortfolioId(portfolio.identifier),
                    randomFactory.nextInt(50) + 1,
                    randomFactory.nextInt(10) + 1)
        } else {
            def availableOrderBook = obtainAvailableOrderBook(portfolio)
            if (availableOrderBook) {
                command = new StartSellTransactionCommand(
                        new OrderBookId(availableOrderBook[0]),
                        new PortfolioId(portfolio.identifier),
                        availableOrderBook[1],
                        randomFactory.nextInt(10) + 1)
            }
        }

        return command
    }

    private def obtainRandomOrderBook() {
        return orderBookEntries[randomFactory.nextInt(orderBookEntries.size())]
    }

    private def obtainAvailableOrderBook(PortfolioEntry portfolioEntry) {
        def amountOfOrderBooks = portfolioEntry.itemsInPossession.size()
        def counterOrderBook = 0
        while (counterOrderBook < amountOfOrderBooks) {
            def identifier = portfolioEntry.itemsInPossession.keySet().toArray()[counterOrderBook]
            def amountAvailable = portfolioEntry.itemsInPossession[identifier].amount
            def reserved = (portfolioEntry.itemsReserved[identifier]) ? portfolioEntry.itemsReserved[identifier].amount : 0
            if (amountAvailable > reserved) {
                def amountToSell = (amountAvailable - reserved > 50) ? randomFactory.nextInt(50) + 1 : amountAvailable - reserved
                return [identifier, amountToSell]
            }
            counterOrderBook++
        }
        return null
    }

}
