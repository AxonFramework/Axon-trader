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

import org.hamcrest.Description;
import org.mockito.*;

public class PortfolioEntryMatcher extends ArgumentMatcher<PortfolioView> {

    private final int itemsInPossession;
    private final String itemIdentifier;
    private final int amountOfItemInPossession;
    private final int itemsInReservation;
    private final int amountOfItemInReservation;

    public PortfolioEntryMatcher(String itemIdentifier,
                                 int itemsInPossession,
                                 int amountOfItemInPossession,
                                 int itemsInReservation,
                                 int amountOfItemInReservation) {
        this.itemsInPossession = itemsInPossession;
        this.itemIdentifier = itemIdentifier;
        this.amountOfItemInPossession = amountOfItemInPossession;
        this.itemsInReservation = itemsInReservation;
        this.amountOfItemInReservation = amountOfItemInReservation;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof PortfolioView)) {
            return false;
        }
        PortfolioView portfolioView = (PortfolioView) argument;

        return portfolioView.getItemsInPossession().size() == itemsInPossession
                && amountOfItemInPossession == portfolioView.findItemInPossession(itemIdentifier).getAmount()
                && portfolioView.getItemsReserved().size() == itemsInReservation
                && !(itemsInReservation != 0 && (amountOfItemInReservation != portfolioView
                .findReservedItemByIdentifier(itemIdentifier).getAmount()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("PortfolioView with itemsInPossession [")
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
