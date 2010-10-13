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

package org.axonframework.samples.trader.webui.order;

import javax.validation.constraints.Min;

/**
 * @author Jettro Coenradie
 */
public class AbstractOrder {
    private String tradeItemId;
    private String tradeItemName;

    @Min(0)
    private long tradeCount;

    @Min(0)
    private int itemPrice;

    public AbstractOrder() {
    }

    public AbstractOrder(int itemPrice, long tradeCount, String tradeItemId, String tradeItemName) {
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.tradeItemId = tradeItemId;
        this.tradeItemName = tradeItemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    public void setTradeItemName(String tradeItemName) {
        this.tradeItemName = tradeItemName;
    }
}
