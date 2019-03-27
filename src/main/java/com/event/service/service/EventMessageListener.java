package com.event.service.service;

import com.event.service.config.ApplicationConfiguration;
import com.event.service.domain.EventRecords;
import com.event.service.repository.EventRecordsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMessageListener {

    @Autowired
    EventRecordsRepository eventRecordsRepository;

    private static Logger logger = LoggerFactory.getLogger(EventMessageListener.class);

    @RabbitListener(queues= ApplicationConfiguration.EVENT_QUEUE)
    public void receivedMessage(EventRecords eventRecords) {
        logger.info("Message received {}", eventRecords);
        eventRecordsRepository.save(eventRecords);
    }
}
