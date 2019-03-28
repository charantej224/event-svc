package com.event.service.service;

import com.event.service.config.ApplicationProperties;
import com.event.service.domain.EventRecords;
import com.event.service.domain.GenericMessage;
import com.event.service.repository.CustomEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.event.service.config.Constants.NUMBER_101;

@Service
public class EventService {

    @Autowired
    CustomEventRepository eventRepository;

    @Autowired
    HttpHeaders httpHeaders;

    @Autowired
    ApplicationProperties applicationProperties;

    private static Logger logger = LoggerFactory.getLogger(EventService.class);

    public ResponseEntity getEventsForEmployee(String emailId) {
        logger.info("Request Recieved with email id {}", emailId);
        try {
            Optional<List<EventRecords>> eventRecords = eventRepository.findByEmailIdOrderByCreatedDateAsc(emailId);
            if (eventRecords.isPresent()) {
                return ResponseEntity.ok().headers(httpHeaders).body(eventRecords.get());
            } else {
                GenericMessage<String> genericMessage = new GenericMessage<>(NUMBER_101, applicationProperties.getMessages().get(NUMBER_101));
                return ResponseEntity.badRequest().headers(httpHeaders).body(genericMessage);
            }
        } catch (RuntimeException runtimeException) {
            logger.error(runtimeException.getMessage(), runtimeException);
            GenericMessage<String> genericMessage = new GenericMessage<>(runtimeException.getClass().getName(), runtimeException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(genericMessage);
        }

    }

}
