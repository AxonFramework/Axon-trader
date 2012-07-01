import groovy.json.JsonSlurper
import org.vertx.groovy.core.http.RouteMatcher

def server = vertx.createHttpServer()
def routeMatcher = new RouteMatcher()
def slurper = new JsonSlurper()
def eb = vertx.eventBus

routeMatcher.post("/executed") { request ->
    println "received a trade executed event"

    request.bodyHandler { body ->
        def received = slurper.parseText(body.toString())
        eb.send("trader.trade.executed", received)
    }

    request.response.putHeader("Content-Type", "application/json")
    request.response.end("{\"status\":\"RECEIVED\"}")
}

server.requestHandler(routeMatcher.asClosure())

vertx.createSockJSServer(server).bridge(prefix: '/eventbus', [[:]])

server.listen(9090)

container.deployVerticle("OrderHandler.groovy") {
    println "OrderHandler verticle is deployed."
}

