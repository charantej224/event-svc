package com.event.service.service;

import com.event.service.config.ApplicationProperties;
import com.event.service.domain.EventRecords;
import com.event.service.domain.GenericMessage;
import com.event.service.repository.CustomEventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    @Mock
    private CustomEventRepository mockEventRepository;
    @Mock
    private HttpHeaders mockHttpHeaders;
    @Mock
    private ApplicationProperties mockApplicationProperties;

    @InjectMocks
    private EventService eventServiceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        EventRecords eventRecord1 = new EventRecords();
        EventRecords eventRecord2 = new EventRecords();
        List<EventRecords> eventRecordsList = new ArrayList<EventRecords>();
        eventRecordsList.add(eventRecord1);
        eventRecordsList.add(eventRecord2);
        Map<String,String> messageMap = new HashMap<String, String>();
        messageMap.put("101","Email id doesn't exist in events database");
        when(mockEventRepository.findByEmailIdOrderByCreatedDateAsc("test@email.com")).thenReturn(Optional.of(eventRecordsList));
        when(mockApplicationProperties.getMessages()).thenReturn(messageMap);
    }

    @Test
    public void testGetEventsForEmployee_returnMockedList_success() {
        final String emailId = "test@email.com";
        final ResponseEntity result = eventServiceUnderTest.getEventsForEmployee(emailId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, ((List)result.getBody()).size());
    }

    @Test
    public void testGetEventsForEmployee_NoData_badRequest(){
        final String emailId = "Something";
        final ResponseEntity result = eventServiceUnderTest.getEventsForEmployee(emailId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("101", ((GenericMessage<String>)result.getBody()).getMessage());
        assertEquals("Email id doesn't exist in events database", ((GenericMessage<String>)result.getBody()).getMessageBody());

    }

    @Test
    public void testGetEventsForEmployee_throwsRuntimeException(){
        when(mockEventRepository.findByEmailIdOrderByCreatedDateAsc("test@email.com")).thenThrow(new RuntimeException("Stubbed Exception"));
        final ResponseEntity result = eventServiceUnderTest.getEventsForEmployee("test@email.com");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("java.lang.RuntimeException", ((GenericMessage<String>)result.getBody()).getMessage());
        assertEquals("Stubbed Exception", ((GenericMessage<String>)result.getBody()).getMessageBody());

    }
}
