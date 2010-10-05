package org.axonframework.samples.trader.app.api.user;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;

/**
 * @author Jettro Coenradie
 */
public class UserAuthenticatedEvent extends DomainEvent {
    public AggregateIdentifier getUserId() {
        return getAggregateIdentifier();
    }
}
