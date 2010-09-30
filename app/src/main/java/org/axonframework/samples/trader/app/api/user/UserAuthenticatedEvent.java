package org.axonframework.samples.trader.app.api.user;

import org.axonframework.domain.DomainEvent;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
public class UserAuthenticatedEvent extends DomainEvent {
    public UUID getUserId() {
        return getAggregateIdentifier();
    }
}
