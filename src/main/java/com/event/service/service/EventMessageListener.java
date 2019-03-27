package com.event.service.service;

import com.event.service.config.ApplicationConfiguration;
import com.event.service.domain.EventRecords;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventMessageListener {

    @RabbitListener(queues= ApplicationConfiguration.EVENT_QUEUE)
    public void receivedMessage(EventRecords eventRecords) {
        System.out.println("Received Message: " + eventRecords);
    }
}
