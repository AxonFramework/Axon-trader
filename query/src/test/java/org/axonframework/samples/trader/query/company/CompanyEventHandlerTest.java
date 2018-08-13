package org.axonframework.samples.trader.query.company;

import org.axonframework.samples.trader.api.company.CompanyCreatedEvent;
import org.axonframework.samples.trader.api.company.CompanyId;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CompanyEventHandlerTest {

    private final CompanyViewRepository companyViewRepository = mock(CompanyViewRepository.class);

    private CompanyEventHandler testSubject;

    @Before
    public void setUp() {
        testSubject = new CompanyEventHandler(companyViewRepository);
    }

    @Test
    public void testOnCompanyCreatedEventACompanyViewIsSaved() {
        CompanyId expectedCompanyId = new CompanyId();
        String expectedCompanyName = "companyName";
        int expectedCompanyValue = 1000;
        int expectedAmountOfShares = 500;

        CompanyCreatedEvent testEvent = new CompanyCreatedEvent(
                expectedCompanyId, expectedCompanyName, expectedCompanyValue, expectedAmountOfShares
        );

        testSubject.on(testEvent);

        ArgumentCaptor<CompanyView> companyViewCaptor = ArgumentCaptor.forClass(CompanyView.class);

        verify(companyViewRepository).save(companyViewCaptor.capture());

        CompanyView result = companyViewCaptor.getValue();
        assertNotNull(result);
        assertEquals(expectedCompanyId.getIdentifier(), result.getIdentifier());
        assertEquals(expectedCompanyName, result.getName());
        assertEquals(expectedCompanyValue, result.getValue());
        assertEquals(expectedAmountOfShares, result.getAmountOfShares());
    }
}