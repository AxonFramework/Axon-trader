package org.axonframework.samples.trader.app.api.tradeitem;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class CreateTradeItemCommand {
    private UUID userId;
    private String tradeItemName;
    private long tradeItemValue;
    private long amountOfShares;

    public CreateTradeItemCommand(UUID userId, String tradeItemName, long tradeItemValue, long amountOfShares) {
        this.amountOfShares = amountOfShares;
        this.tradeItemName = tradeItemName;
        this.tradeItemValue = tradeItemValue;
        this.userId = userId;
    }

    public long getAmountOfShares() {
        return amountOfShares;
    }

    public String getTradeItemName() {
        return tradeItemName;
    }

    public long getTradeItemValue() {
        return tradeItemValue;
    }

    public UUID getUserId() {
        return userId;
    }
}
