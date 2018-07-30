package org.axonframework.samples.trader.query.kds;

import org.axonframework.samples.trader.api.kds.StallCreatedEvent;
import org.axonframework.samples.trader.api.kds.StallId;
import org.axonframework.samples.trader.query.kds.repositories.StallViewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StallEventHandlerTest {

    private final StallViewRepository stallViewRepository = mock(StallViewRepository.class);

    private StallEventHandler testSubject;

    @Before
    public void setUp() {
        testSubject = new StallEventHandler(stallViewRepository);
    }

    @Test
    public void testOnCompanyCreatedEventACompanyViewIsSaved() {
        StallId expectedStallId = new StallId();
        long expectedPoiId = 2L;
        String expectedStallName = "stallName";
        String expectedRemark = "remark";
        String expectedRequirements = "requirements";
        int expectedAbility = 10;

        StallCreatedEvent testEvent = new StallCreatedEvent(expectedStallId, expectedPoiId, expectedStallName, expectedRemark, expectedRequirements, expectedAbility
        );

        testSubject.on(testEvent);

        ArgumentCaptor<StallView> stallViewCaptor = ArgumentCaptor.forClass(StallView.class);

        verify(stallViewRepository).save(stallViewCaptor.capture());

        StallView result = stallViewCaptor.getValue();
        assertNotNull(result);
        assertEquals(expectedStallId.toString(), result.getIdentifier());
        assertEquals(expectedPoiId, result.getPoiId());
        assertEquals(expectedStallName, result.getStallName());
        assertEquals(expectedRemark, result.getRemark());
        assertEquals(expectedRequirements, result.getRequirements());
        assertEquals(expectedAbility, result.getAbility());
    }

}
