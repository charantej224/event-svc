package com.event.service.service;

import com.event.service.domain.EventRecords;
import com.event.service.repository.EventRecordsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class EventMessageListenerTest {

    @Mock
    private EventRecordsRepository mockEventRecordsRepository;

    @InjectMocks
    private EventMessageListener eventMessageListenerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testReceivedMessage() {
        final EventRecords eventRecords = new EventRecords();
        eventMessageListenerUnderTest.receivedMessage(eventRecords);
        verify(mockEventRecordsRepository, times(1)).save(eventRecords);
    }

    @Test(expected = RuntimeException.class)
    public void testReceivedMessage_throwsRuntimeException() {
        final EventRecords eventRecords = new EventRecords();
        when(mockEventRecordsRepository.save(eventRecords)).thenThrow(new RuntimeException("mocked exception"));
        eventMessageListenerUnderTest.receivedMessage(eventRecords);
    }
}
