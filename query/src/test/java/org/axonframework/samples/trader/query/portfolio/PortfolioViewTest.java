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

package org.axonframework.samples.trader.query.portfolio;

import org.junit.*;

import static org.junit.Assert.*;

public class PortfolioViewTest {

    private static final long AMOUNT_ITEMS = 100;
    private static final long AMOUNT_RESERVED = 40;
    private static final int AMOUNT_SELL = 10;
    private static final String ORDER_BOOK_ID = "item1";
    private static final int AMOUNT_OF_MONEY = 1000;
    private static final int RESERVED_AMOUNT_OF_MONEY = 200;

    @Test
    public void testRemovingItems() {
        PortfolioView portfolio = createDefaultPortfolio();

        portfolio.removeReservedItem(ORDER_BOOK_ID, AMOUNT_SELL);
        portfolio.removeItemsInPossession(ORDER_BOOK_ID, AMOUNT_SELL);

        assertEquals(AMOUNT_RESERVED - AMOUNT_SELL, portfolio.findReservedItemByIdentifier(ORDER_BOOK_ID).getAmount());
        assertEquals(AMOUNT_ITEMS - AMOUNT_SELL, portfolio.findItemInPossession(ORDER_BOOK_ID).getAmount());
    }

    @Test
    public void testObtainAvailableItems() {
        PortfolioView portfolio = createDefaultPortfolio();

        assertEquals(AMOUNT_ITEMS - AMOUNT_RESERVED, portfolio.obtainAmountOfAvailableItemsFor(ORDER_BOOK_ID));
    }

    @Test
    public void testObtainBudget() {
        PortfolioView portfolio = createDefaultPortfolio();
        assertEquals(AMOUNT_OF_MONEY - RESERVED_AMOUNT_OF_MONEY, portfolio.obtainMoneyToSpend());
    }

    private PortfolioView createDefaultPortfolio() {
        PortfolioView portfolio = new PortfolioView();

        portfolio.addItemInPossession(createItem(AMOUNT_ITEMS));
        portfolio.addReservedItem(createItem(AMOUNT_RESERVED));
        portfolio.setAmountOfMoney(AMOUNT_OF_MONEY);
        portfolio.setReservedAmountOfMoney(RESERVED_AMOUNT_OF_MONEY);
        return portfolio;
    }

    private ItemEntry createItem(long amount) {
        ItemEntry item1InPossession = new ItemEntry();
        item1InPossession.setIdentifier("item1");
        item1InPossession.setAmount(amount);
        item1InPossession.setCompanyIdentifier("company1");
        item1InPossession.setCompanyName("Company One");
        return item1InPossession;
    }
}
