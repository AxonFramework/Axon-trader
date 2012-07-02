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

import groovy.json.JsonSlurper
import org.vertx.groovy.core.http.RouteMatcher

def logger = container.logger

def server = vertx.createHttpServer()
def routeMatcher = new RouteMatcher()
def slurper = new JsonSlurper()
def eventBus = vertx.eventBus

// Configure the router to accept POST requests to the /executed url. The request must contain a json object with the
// event. The following code block shows what it looks like.
//{
//    tradeExecuted :
//    {
//        orderbookId: ... ,
//        count: ... ,
//        price: ...
//    }
//}
routeMatcher.post("/executed") { request ->
    logger.info "received a trade executed event from the trader application"

    request.bodyHandler { body ->
        def received = slurper.parseText(body.toString())
        eventBus.send("trader.trade.executed", received)
    }

    request.response.putHeader("Content-Type", "application/json")
    request.response.end("{\"status\":\"RECEIVED\"}")
}

server.requestHandler(routeMatcher.asClosure())

// Setup the Sockjs connection
vertx.createSockJSServer(server).bridge(prefix: '/eventbus', [[:]])

server.listen(9090)

container.deployVerticle("OrderHandler.groovy") {
    logger.info "OrderHandler verticle is deployed."
}

