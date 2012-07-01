def eventBus = vertx.eventBus
def companies = [:]

eventBus.registerHandler("trader.trade.executed") { message ->
    println "Order: ${message.body}"
    def result = message.body
    result.tradeExecuted.companyName = companies.get(result.tradeExecuted.orderbookId)
    eventBus.send("updates.trades", message.body)
}



def mongoConfig = ["db_name": "axontrader"]
container.with {
    deployVerticle('mongo-persistor', mongoConfig, 1) {
        println "Mongo is deployed"
        def query = ["action": "find", "collection": "orderBookEntry", "matcher": [:]]
        eventBus.send("vertx.mongopersistor", query) {message ->
            message.body.results.each {orderbook ->
                println "Add company for orderbook: ${orderbook._id} - ${orderbook.companyName}"
                companies.put(orderbook._id, orderbook.companyName)
            }
        }
    }
}