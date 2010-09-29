package org.axonframework.samples.trader.app.command.user;

import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jettro Coenradie
 */
public class UserCommandHandlerTest {
    private FixtureConfiguration fixture;

    @Before
    public void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture();
        UserCommandHandler commandHandler = new UserCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(User.class));
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }


    @Test
    public void testHandleCreateUser() throws Exception {
        fixture.given()
            .when(new CreateUserCommand("Buyer 1", "buyer1", "buyer1"))
            .expectEvents(new UserCreatedEvent("Buyer 1", "buyer1", "buyer1"));
    }
}
