package org.axonframework.samples.trader.query.users;

import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.samples.trader.query.users.repositories.UserViewRepository;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserEventHandlerTest {

    private final UserViewRepository userViewRepository = mock(UserViewRepository.class);

    private UserEventHandler testSubject;

    @Before
    public void setUp() {
        testSubject = new UserEventHandler(userViewRepository);
    }

    @Test
    public void testOnUserCreatedEventAnUserViewIsSaved() {
        UserId expectedUserId = new UserId();
        String expectedName = "name";
        String expectedUserName = "userName";

        UserCreatedEvent testEvent = new UserCreatedEvent(expectedUserId, expectedName, expectedUserName, "password");

        testSubject.on(testEvent);

        ArgumentCaptor<UserView> userViewCaptor = ArgumentCaptor.forClass(UserView.class);

        verify(userViewRepository).save(userViewCaptor.capture());

        UserView result = userViewCaptor.getValue();
        assertNotNull(result);
        assertEquals(expectedUserId.toString(), result.getUserId());
        assertEquals(expectedName, result.getName());
        assertEquals(expectedUserName, result.getUserName());
    }
}