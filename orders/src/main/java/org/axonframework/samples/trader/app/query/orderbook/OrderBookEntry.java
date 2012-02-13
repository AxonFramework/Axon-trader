/*
 * Copyright (c) 2010. Gridshore
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

package org.axonframework.samples.trader.app.query.orderbook;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
public class OrderBookEntry {
    @Id
    private String identifier;
    private String companyIdentifier;
    private String companyName;
    private List<OrderEntry> sellOrders = new ArrayList<OrderEntry>();
    private List<OrderEntry> buyOrders = new ArrayList<OrderEntry>();

    public List<OrderEntry> sellOrders() {
        return sellOrders;
    }

    public List<OrderEntry> buyOrders() {
        return buyOrders;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCompanyIdentifier() {
        return companyIdentifier;
    }

    public void setCompanyIdentifier(String companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<OrderEntry> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<OrderEntry> buyOrders) {
        this.buyOrders = buyOrders;
    }

    public List<OrderEntry> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<OrderEntry> sellOrders) {
        this.sellOrders = sellOrders;
    }
}
