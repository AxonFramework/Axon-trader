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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.api.orders.trades.TradeExecutedEvent;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>Creates a JSON object and sends it to the configured server using http post. The structure of the json object is:</p>
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
public class OrderbookExternalListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderbookExternalListener.class);

    private JsonFactory jsonFactory = new JsonFactory();
    private String remoteServerUri;

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

        HttpPost post = new HttpPost(remoteServerUri);
        post.setEntity(new StringEntity(jsonObjectAsString));
        post.addHeader("Content-Type", "application/json");

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() != 200) {
            Writer writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            logger.warn("Error while sending event to external system: {}", writer.toString());
        }

    }

    private String createJsonInString(TradeExecutedEvent event) throws IOException {
        Writer writer = new StringWriter();
        JsonGenerator g = jsonFactory.createJsonGenerator(writer);
        g.writeStartObject();
        g.writeObjectFieldStart("tradeExecuted");
        g.writeStringField("orderbookId", event.getOrderBookIdentifier().toString());
        g.writeStringField("count", String.valueOf(event.getTradeCount()));
        g.writeStringField("price", String.valueOf(event.getTradePrice()));
        g.writeEndObject(); // for trade-executed
        g.close();
        return writer.toString();
    }

    @Value("#{external.serverUrlExecutedTrades}")
    public void setRemoteServerUri(String remoteServerUri) {
        this.remoteServerUri = remoteServerUri;
    }
}
