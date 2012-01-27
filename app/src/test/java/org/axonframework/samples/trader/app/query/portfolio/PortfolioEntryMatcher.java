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

package org.axonframework.samples.trader.app.query.portfolio;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

/**
 * @author Jettro Coenradie
 */
public class PortfolioEntryMatcher extends ArgumentMatcher<PortfolioEntry> {
    private int itemsInPossession;
    private String itemIdentifier;
    private int amountOfItemInPossession;
    private int itemsInReservation;
    private int amountOfItemInReservation;

    public PortfolioEntryMatcher(String itemIdentifier, int itemsInPossession, int amountOfItemInPossession, int itemsInReservation, int amountOfItemInReservation) {
        this.itemsInPossession = itemsInPossession;
        this.itemIdentifier = itemIdentifier;
        this.amountOfItemInPossession = amountOfItemInPossession;
        this.itemsInReservation = itemsInReservation;
        this.amountOfItemInReservation = amountOfItemInReservation;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof PortfolioEntry)) {
            return false;
        }
        PortfolioEntry portfolioEntry = (PortfolioEntry) argument;

        return portfolioEntry.getItemsInPossession().size() == itemsInPossession
                && amountOfItemInPossession == portfolioEntry.findItemInPossession(itemIdentifier).getAmount()
                && portfolioEntry.getItemsReserved().size() == itemsInReservation
                && !(itemsInReservation != 0 && (amountOfItemInReservation != portfolioEntry.findReservedItemByIdentifier(itemIdentifier).getAmount()));

    }

    @Override
    public void describeTo(Description description) {
        description.appendText("PortfolioEntry with itemsInPossession [")
                .appendValue(itemsInPossession)
                .appendText("] and amountOfItemsInPossession [")
                .appendValue(amountOfItemInPossession)
                .appendText("] and amountOfItemsInReservation [")
                .appendValue(amountOfItemInReservation)
                .appendText("] and itemsInReservation [")
                .appendValue(itemsInReservation)
                .appendText("]");

    }
}
