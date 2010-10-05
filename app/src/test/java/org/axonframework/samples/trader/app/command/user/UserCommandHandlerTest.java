package org.axonframework.samples.trader.app.command.user;

import org.axonframework.samples.trader.app.api.user.AuthenticateUserCommand;
import org.axonframework.samples.trader.app.api.user.CreateUserCommand;
import org.axonframework.samples.trader.app.api.user.UserAuthenticatedEvent;
import org.axonframework.samples.trader.app.api.user.UserCreatedEvent;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.UserRepository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.samples.trader.app.util.DigestUtils.sha1;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jettro Coenradie
 */
public class UserCommandHandlerTest {
    private FixtureConfiguration fixture;

    private UserRepository userQueryRepository;

    @Before
    public void setUp() {
        userQueryRepository = mock(UserRepository.class);

        fixture = Fixtures.newGivenWhenThenFixture();
        UserCommandHandler commandHandler = new UserCommandHandler();
        commandHandler.setRepository(fixture.createGenericRepository(User.class));
        commandHandler.setUserRepository(userQueryRepository);
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }


    @Test
    public void testHandleCreateUser() throws Exception {
        fixture.given()
            .when(new CreateUserCommand("Buyer 1", "buyer1", "buyer1"))
            .expectEvents(new UserCreatedEvent("Buyer 1", "buyer1", sha1("buyer1")));
    }

    @Test
    public void testHandleAuthenticateUser() throws Exception {
        UserEntry userEntry = new UserEntry();
        userEntry.setUsername("buyer1");
        userEntry.setIdentifier(fixture.getAggregateIdentifier().asString());
        userEntry.setName("Buyer One");
        when(userQueryRepository.findByUsername("buyer1")).thenReturn(userEntry);

        fixture.given(new UserCreatedEvent("Buyer 1", "buyer1", sha1("buyer1")))
            .when(new AuthenticateUserCommand("buyer1", "buyer1".toCharArray()))
            .expectEvents(new UserAuthenticatedEvent());
    }
}
