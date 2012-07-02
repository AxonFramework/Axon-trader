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
def logger = container.logger
def eventBus = vertx.eventBus
def companies = [:] // Map containing the names of the companies with the id of the orderbook as a key

eventBus.registerHandler("trader.trade.executed") { message ->
    def result = message.body
    logger.info "Received an executed trade : ${result}"
    // Add the name of the company to the obtained result
    result.tradeExecuted.companyName = companies.get(result.tradeExecuted.orderbookId)

    // Put a new message with the result on the event bus to send it to subscribers
    eventBus.send("updates.trades", result)
}



def mongoConfig = ["db_name": "axontrader"]
container.with {
    deployVerticle('mongo-persistor', mongoConfig, 1) {
        logger.info "Mongo busmod is deployed"

        // Query for all orderBookEntries to fill the map with orderbookId and Company Name
        def query = ["action": "find", "collection": "orderBookEntry", "matcher": [:]]
        eventBus.send("vertx.mongopersistor", query) {message ->
            message.body.results.each {orderbook ->
                logger.info "Add company for orderbook: ${orderbook._id} - ${orderbook.companyName}"
                companies.put(orderbook._id, orderbook.companyName)
            }
        }
    }
}