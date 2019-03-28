package com.event.service.web.rest;

import com.event.service.domain.EventRecords;
import com.event.service.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class EventRecordsResourceTest {

    @Mock
    private EventService mockEventService;

    @InjectMocks
    private EventRecordsResource eventRecordsResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        when(mockEventService.getEventsForEmployee("emailId")).thenReturn(new ResponseEntity(new ArrayList<>(), HttpStatus.OK));
    }

    @Test
    public void testGetEventRecords() {
        final String emailId = "emailId";
        final ResponseEntity<EventRecords> expectedResult = null;
        final ResponseEntity<EventRecords> result = eventRecordsResourceUnderTest.getEventRecords(emailId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
