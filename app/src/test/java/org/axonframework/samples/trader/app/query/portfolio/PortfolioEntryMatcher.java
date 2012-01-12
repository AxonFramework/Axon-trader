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
    private String problem;
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
            problem = String.format("Wrong argument type, required %s but received %s", PortfolioEntry.class.getName(), argument.getClass().getName());
            return false;
        }
        PortfolioEntry portfolioEntry = (PortfolioEntry) argument;

        if (portfolioEntry.getItemsInPossession().size() != itemsInPossession) {
            problem = String.format("Amount of item entries in possession should be %d but was %d", itemsInPossession, portfolioEntry.getItemsInPossession().size());
            return false;
        }
        long foundAmountOfItemsInPossession = portfolioEntry.findItemInPossession(itemIdentifier).getAmount();
        if (foundAmountOfItemsInPossession != amountOfItemInPossession) {
            problem = String.format("The amount of the item in possession should be %d but was %d", amountOfItemInPossession, foundAmountOfItemsInPossession);
            return false;
        }
        if (portfolioEntry.getItemsReserved().size() != itemsInReservation) {
            problem = String.format("The amount of reserved item entries should be %d but was %d", itemsInReservation, portfolioEntry.getItemsReserved().size());
            return false;
        }
        if (itemsInReservation != 0) {
            long foundAmountOfItemsInReservation = portfolioEntry.findReservedItemByIdentifier(itemIdentifier).getAmount();
            if (foundAmountOfItemsInReservation != amountOfItemInReservation) {
                problem = String.format("The amount of the reserved items should be %d but was %d", amountOfItemInReservation, foundAmountOfItemsInReservation);
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(problem);
    }
}
