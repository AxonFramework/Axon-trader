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

package org.axonframework.samples.trader.listener;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>Creates a JSON object and broadcasts it to every connected WebSocket session. The structure of the json object is:</p>
 * <pre>
 * {
 *     tradeExecuted :
 *     {
 *         orderbookId: ... ,
 *         count: ... ,
 *         price: ...
 *     }
 * }
 * </pre>
 * <p>The url to send the request to can be configured.</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class ExecutedTradesBroadcaster extends BroadcastingTextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExecutedTradesBroadcaster.class);

    private JsonFactory jsonFactory = new JsonFactory();

    @EventHandler
    public void handle(TradeExecutedEvent event) {
        try {
            doHandle(event);
        } catch (IOException e) {
            logger.warn("Problem while sending TradeExecutedEvent to external system");
        }
    }

    private void doHandle(TradeExecutedEvent event) throws IOException {
        String jsonObjectAsString = createJsonInString(event);

        this.broadcast(jsonObjectAsString);
    }

    private String createJsonInString(TradeExecutedEvent event) throws IOException {
        Writer writer = new StringWriter();
        JsonGenerator g = jsonFactory.createJsonGenerator(writer);
        g.writeStartObject();
        g.writeObjectFieldStart("tradeExecuted");
        g.writeStringField("orderbookId", event.getOrderBookId().toString());
        g.writeStringField("count", String.valueOf(event.getTradeCount()));
        g.writeStringField("price", String.valueOf(event.getTradePrice()));
        g.writeEndObject(); // for trade-executed
        g.close();
        return writer.toString();
    }
}
