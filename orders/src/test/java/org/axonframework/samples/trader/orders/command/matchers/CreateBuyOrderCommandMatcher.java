/*
 * Copyright (c) 2010-2012. Axon Framework
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

package org.axonframework.samples.trader.orders.command.matchers;

import org.axonframework.samples.trader.api.orders.trades.CreateBuyOrderCommand;
import org.axonframework.samples.trader.api.orders.trades.OrderBookId;
import org.axonframework.samples.trader.api.orders.trades.PortfolioId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * @author Jettro Coenradie
 */
public class CreateBuyOrderCommandMatcher extends BaseCommandMatcher<CreateBuyOrderCommand> {

    private OrderBookId orderbookIdentifier;
    private PortfolioId portfolioIdentifier;
    private long tradeCount;
    private long itemPrice;

    private CreateBuyOrderCommandMatcher(PortfolioId portfolioId, OrderBookId orderbookId, long tradeCount, long itemPrice) {
        this.portfolioIdentifier = portfolioId;
        this.orderbookIdentifier = orderbookId;
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
    }

    public static Matcher newInstance(PortfolioId portfolioIdentifier, OrderBookId orderbookIdentifier, long totalItems, long pricePerItem) {
        return new CreateBuyOrderCommandMatcher(portfolioIdentifier, orderbookIdentifier, totalItems, pricePerItem);
    }

    @Override
    protected boolean doMatches(CreateBuyOrderCommand command) {
        return command.getOrderBookId().equals(orderbookIdentifier)
                && command.getPortfolioId().equals(portfolioIdentifier)
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
