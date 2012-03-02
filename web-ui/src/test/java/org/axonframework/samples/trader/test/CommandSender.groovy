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

import com.thoughtworks.xstream.XStream
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import org.axonframework.samples.trader.query.portfolio.PortfolioEntry
import org.axonframework.samples.trader.query.orderbook.OrderBookEntry

/**
 * This class serializes the command using xstream and sends it to the server.
 *
 * @author Jettro Coenradie
 */
class CommandSender {
    def http = new HTTPBuilder('http://localhost:8080/')
    def requestContentType = ContentType.URLENC
    XStream xStream = new XStream()

    def sendCommand(commandToSend) {
        def xml = xStream.toXML(commandToSend)
        def postBody = [command: xml]

        http.post(path: 'rest/command', body: postBody, requestContentType: requestContentType) { resp ->
            assert resp.statusLine.statusCode == 200
        }
    }

    def obtainPortfolios() {
        http.get(path: 'rest/portfolio', requestContentType: requestContentType) { resp, reader ->
            def xmlData = reader.text
            List<PortfolioEntry> portfolios = xStream.fromXML(xmlData)
            return portfolios
        }
    }

    def obtainOrderBooks() {
        http.get(path: 'rest/orderbook', requestContentType: requestContentType) { resp, reader ->
            def xmlData = reader.text
            List<OrderBookEntry> orderBookEntries = xStream.fromXML(xmlData)
            return orderBookEntries
        }
    }

    def obtainPortfolio(identifier) {
        http.get(path: 'rest/portfolio/' + identifier, requestContentType: requestContentType) { resp, reader ->
            def xmlData = reader.text
            PortfolioEntry entry = xStream.fromXML(xmlData)
            return entry
        }
    }
}
