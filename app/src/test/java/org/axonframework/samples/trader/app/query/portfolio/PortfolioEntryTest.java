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

package org.axonframework.samples.trader.app.query.portfolio;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jettro Coenradie
 */
public class PortfolioEntryTest {
    @Test
    public void testRemovingItems() {
        PortfolioEntry portfolio = new PortfolioEntry();
        ItemEntry item1InPossession = new ItemEntry();
        item1InPossession.setIdentifier("item1");
        item1InPossession.setAmount(100);
        item1InPossession.setCompanyIdentifier("company1");
        item1InPossession.setCompanyName("Company One");
        portfolio.addItemInPossession(item1InPossession);

        ItemEntry item1InReservation = new ItemEntry();
        item1InReservation.setIdentifier("item1");
        item1InReservation.setAmount(33);
        item1InReservation.setCompanyIdentifier("company1");
        item1InReservation.setCompanyName("Company One");
        portfolio.addReservedItem(item1InReservation);

        portfolio.removeReservedItem("item1", 11);
        assertEquals(22, portfolio.findReservedItemByIdentifier("item1").getAmount());

        portfolio.removeItemsInPossession("item1", 11);
        assertEquals(89, portfolio.findItemInPossession("item1").getAmount());
    }


}
