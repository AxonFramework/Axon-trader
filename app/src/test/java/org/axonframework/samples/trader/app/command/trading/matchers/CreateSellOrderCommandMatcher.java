/*
 * Copyright (c) 2011. Gridshore
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

package org.axonframework.samples.trader.app.command.trading.matchers;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.samples.trader.app.api.order.CreateSellOrderCommand;

/**
 * @author Jettro Coenradie
 */
public class CreateSellOrderCommandMatcher extends TradeManagerSagaMatcher<CreateSellOrderCommand> {
    private String orderbookIdentifier;
    private String portfolioIdentifier;
    private long tradeCount;
    private int itemPrice;

    public CreateSellOrderCommandMatcher(AggregateIdentifier portfolioIdentifier, AggregateIdentifier orderbookIdentifier, long tradeCount, int itemPrice) {
        this.portfolioIdentifier = portfolioIdentifier.asString();
        this.orderbookIdentifier = orderbookIdentifier.asString();
        this.tradeCount = tradeCount;
        this.itemPrice = itemPrice;
    }

    @Override
    public boolean doMatch(CreateSellOrderCommand command) {
        if (!command.getOrderBookId().asString().equals(orderbookIdentifier)) {
            problem = String.format("Orderbook identifier is not as expected, required %s but received %s", orderbookIdentifier, command.getOrderBookId());
            return false;
        }
        if (!command.getPortfolioId().asString().equals(portfolioIdentifier)) {
            problem = String.format("Portfolio identifier is not as expected, required %s but received %s", portfolioIdentifier, command.getPortfolioId());
            return false;
        }
        if (tradeCount != command.getTradeCount()) {
            problem = String.format("The amount of items to trade is not as expected, required %d but received %d", tradeCount, command.getTradeCount());
            return false;
        }
        if (itemPrice != command.getItemPrice()) {
            problem = String.format("The price of items to trade is not as exepcted, required %d but received %d", itemPrice, command.getItemPrice());
            return false;
        }
        return true;
    }
}
