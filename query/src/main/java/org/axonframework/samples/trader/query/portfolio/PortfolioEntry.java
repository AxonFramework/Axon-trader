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

package org.axonframework.samples.trader.query.portfolio;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jettro Coenradie
 */
public class PortfolioEntry {
    @Id
    private String identifier;
    private String userIdentifier;
    private String userName;
    private long amountOfMoney;
    private long reservedAmountOfMoney;

    private Map<String, ItemEntry> itemsInPossession = new HashMap<String, ItemEntry>();
    private Map<String, ItemEntry> itemsReserved = new HashMap<String, ItemEntry>();

    /*-------------------------------------------------------------------------------------------*/
    /* utility functions                                                                         */
    /*-------------------------------------------------------------------------------------------*/
    public long obtainAmountOfAvailableItemsFor(String identifier) {
        long possession = obtainAmountOfItemsInPossessionFor(identifier);
        long reserved = obtainAmountOfReservedItemsFor(identifier);
        return possession - reserved;
    }

    public long obtainAmountOfReservedItemsFor(String identifier) {
        ItemEntry item = findReservedItemByIdentifier(identifier);
        if (null == item) {
            return 0;
        }
        return item.getAmount();
    }

    public long obtainAmountOfItemsInPossessionFor(String identifier) {
        ItemEntry item = findItemInPossession(identifier);
        if (null == item) {
            return 0;
        }
        return item.getAmount();
    }

    public long obtainMoneyToSpend() {
        return amountOfMoney - reservedAmountOfMoney;
    }

    public ItemEntry findReservedItemByIdentifier(String identifier) {
        return itemsReserved.get(identifier);
    }

    public ItemEntry findItemInPossession(String identifier) {
        return itemsInPossession.get(identifier);
    }

    public void addReservedItem(ItemEntry itemEntry) {
        handleAdd(itemsReserved, itemEntry);
    }

    public void addItemInPossession(ItemEntry itemEntry) {
        handleAdd(itemsInPossession, itemEntry);
    }

    public void removeReservedItem(String itemIdentifier, long amount) {
        handleRemoveItem(itemsReserved, itemIdentifier, amount);
    }

    public void removeItemsInPossession(String itemIdentifier, long amount) {
        handleRemoveItem(itemsInPossession, itemIdentifier, amount);
    }

    /*-------------------------------------------------------------------------------------------*/
    /* Getters and setters                                                                       */
    /*-------------------------------------------------------------------------------------------*/
    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getReservedAmountOfMoney() {
        return reservedAmountOfMoney;
    }

    public void setReservedAmountOfMoney(long reservedAmountOfMoney) {
        this.reservedAmountOfMoney = reservedAmountOfMoney;
    }

    public Map<String, ItemEntry> getItemsInPossession() {
        return itemsInPossession;
    }

    public void setItemsInPossession(Map<String, ItemEntry> itemsInPossession) {
        this.itemsInPossession = itemsInPossession;
    }

    public Map<String, ItemEntry> getItemsReserved() {
        return itemsReserved;
    }

    public void setItemsReserved(Map<String, ItemEntry> itemsReserved) {
        this.itemsReserved = itemsReserved;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*-------------------------------------------------------------------------------------------*/
    /* Private helper methods                                                                    */
    /*-------------------------------------------------------------------------------------------*/
    private void handleAdd(Map<String, ItemEntry> items, ItemEntry itemEntry) {
        if (items.containsKey(itemEntry.getIdentifier())) {
            ItemEntry foundEntry = items.get(itemEntry.getIdentifier());
            foundEntry.setAmount(foundEntry.getAmount() + itemEntry.getAmount());
        } else {
            items.put(itemEntry.getIdentifier(), itemEntry);
        }
    }

    private void handleRemoveItem(Map<String, ItemEntry> items, String itemIdentifier, long amount) {
        if (items.containsKey(itemIdentifier)) {
            ItemEntry foundEntry = items.get(itemIdentifier);
            foundEntry.setAmount(foundEntry.getAmount() - amount);
            if (foundEntry.getAmount() <= 0) {
                items.remove(foundEntry.getIdentifier());
            }
        }
    }

    @Override
    public String toString() {
        return "PortfolioEntry{" +
                "amountOfMoney=" + amountOfMoney +
                ", identifier='" + identifier + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                ", userName='" + userName + '\'' +
                ", reservedAmountOfMoney=" + reservedAmountOfMoney +
                ", itemsInPossession=" + itemsInPossession +
                ", itemsReserved=" + itemsReserved +
                '}';
    }
}
