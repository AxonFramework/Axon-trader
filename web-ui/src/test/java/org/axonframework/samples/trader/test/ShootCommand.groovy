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

/**
 * For some reason groovy finds the log4j.xml from the httpbuilder jar. Therefore we configure the logging to come
 * from a log4j.properties file on the classpath.
 *
 * -Dlog4j.debug=true -Dlog4j.configuration=log4j.properties
 *
 * @author Jettro Coenradie
 */

import org.axonframework.samples.trader.query.portfolio.PortfolioEntry

def commandSender = new CommandSender()

def portfolios = []
commandSender.obtainPortfolios().each() {
    portfolios.add it.identifier
}

def companyNames = [:]
def orderBooks = []
commandSender.obtainOrderBooks().each() {
    orderBooks.add it.identifier
    companyNames.put(it.identifier, it.companyName)
}
def commandCreator = new CommandCreator(orderBooks)

def numUsers = portfolios.size()
def numUser = 1;

for (int i = 0; i < 1000; i++) {
    def portfolioIdentifier = portfolios[numUser - 1]
    PortfolioEntry portfolio = commandSender.obtainPortfolio(portfolioIdentifier)
    def command = commandCreator.createCommand(portfolio)

    println "${portfolio.userName} # ${command.tradeCount} \$ ${command.itemPrice} ${companyNames[command.orderbookIdentifier.toString()]}"

    commandSender.sendCommand(command)

    if (numUser < numUsers) {
        numUser++
    } else {
        numUser = 1
    }

    // Take a breath
    Thread.sleep(100)
}

