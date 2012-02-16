/*
 * Copyright (c) 2012. Gridshore
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

package org.axonframework.samples.trader.orders.command.matchers;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.tradeengine.api.order.CreateBuyOrderCommand;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * @author Jettro Coenradie
 */
public class CreateBuyOrderCommandMatcher extends BaseMatcher<CreateBuyOrderCommand> {
    private String orderbookIdentifier;
    private String portfolioIdentifier;
    private long tradeCount;
    private long itemPrice;

    public CreateBuyOrderCommandMatcher(AggregateIdentifier portfolioIdentifier, AggregateIdentifier orderbookIdentifier, long tradeCount, long itemPrice) {
        this.portfolioIdentifier = portfolioIdentifier.asString();
        this.orderbookIdentifier = orderbookIdentifier.asString();
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
    }


    @Override
    public boolean matches(Object object) {
        if (!(object instanceof CreateBuyOrderCommand)) {
            return false;
        }
        CreateBuyOrderCommand command = (CreateBuyOrderCommand) object;
        return command.getOrderBookId().asString().equals(orderbookIdentifier)
                && command.getPortfolioId().asString().equals(portfolioIdentifier)
                && tradeCount == command.getTradeCount()
                && itemPrice == command.getItemPrice();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("CreateBuyOrderCommand with tradeCount [")
                .appendValue(tradeCount)
                .appendText("], itemPrice [")
                .appendValue(itemPrice)
                .appendText("] for OrderBook with identifier [")
                .appendValue(orderbookIdentifier)
                .appendText("] and for Portfolio with identifier [")
                .appendValue(portfolioIdentifier)
                .appendText("]");
    }

}
