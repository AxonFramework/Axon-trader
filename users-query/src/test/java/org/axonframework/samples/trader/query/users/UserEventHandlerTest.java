package org.axonframework.samples.trader.query.users;

import org.axonframework.samples.trader.api.users.UserCreatedEvent;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserEventHandlerTest {

    private final UserQueryRepository userQueryRepository = mock(UserQueryRepository.class);

    private UserEventHandler testSubject;

    @Before
    public void setUp() {
        testSubject = new UserEventHandler(userQueryRepository);
    }

    @Test
    public void testOnUserCreatedEventAnUserEntryIsSaved() {
        UserId expectedUserId = new UserId();
        String expectedName = "name";
        String expectedUserName = "userName";

        UserCreatedEvent testEvent = new UserCreatedEvent(expectedUserId, expectedName, expectedUserName, "password");

        testSubject.on(testEvent);

        ArgumentCaptor<UserEntry> userViewCaptor = ArgumentCaptor.forClass(UserEntry.class);

        verify(userQueryRepository).save(userViewCaptor.capture());

        UserEntry result = userViewCaptor.getValue();
        assertNotNull(result);
        assertEquals(expectedUserId.toString(), result.getUserId());
        assertEquals(expectedName, result.getName());
        assertEquals(expectedUserName, result.getUserName());
    }
}