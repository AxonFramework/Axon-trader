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

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jettro Coenradie
 */
@Entity
public class PortfolioEntry {

    @Id
    @javax.persistence.Id
    private String identifier;
    private String userIdentifier;
    private String userName;
    private long amountOfMoney;
    private long reservedAmountOfMoney;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "PORTFOLIO_ITEM_POSSESSION", joinColumns = @JoinColumn(name = "PORTFOLIO_ID"), inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private Map<String, ItemEntry> itemsInPossession = new HashMap<String, ItemEntry>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "PORTFOLIO_ITEM_RESERVED", joinColumns = @JoinColumn(name = "PORTFOLIO_ID"), inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private Map<String, ItemEntry> itemsReserved = new HashMap<String, ItemEntry>();

    /*-------------------------------------------------------------------------------------------*/
    /* utility functions                                                                         */
    /*-------------------------------------------------------------------------------------------*/
    public long obtainAmountOfAvailableItemsFor(String orderbookIdentifier) {
        long possession = obtainAmountOfItemsInPossessionFor(orderbookIdentifier);
        long reserved = obtainAmountOfReservedItemsFor(orderbookIdentifier);
        return possession - reserved;
    }

    public long obtainAmountOfReservedItemsFor(String orderbookIdentifier) {
        ItemEntry item = findReservedItemByIdentifier(orderbookIdentifier);
        if (null == item) {
            return 0;
        }
        return item.getAmount();
    }

    public long obtainAmountOfItemsInPossessionFor(String orderbookIdentifier) {
        ItemEntry item = findItemInPossession(orderbookIdentifier);
        if (null == item) {
            return 0;
        }
        return item.getAmount();
    }

    public long obtainMoneyToSpend() {
        return amountOfMoney - reservedAmountOfMoney;
    }

    public ItemEntry findReservedItemByIdentifier(String orderbookIdentifier) {
        return itemsReserved.get(orderbookIdentifier);
    }

    public ItemEntry findItemInPossession(String orderbookIdentifier) {
        return itemsInPossession.get(orderbookIdentifier);
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
