package com.event.service.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.event.service.domain.EventRecords;
import com.event.service.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing EventRecords.
 */
@RestController
@RequestMapping("/api")
public class EventRecordsResource {

    private final Logger logger = LoggerFactory.getLogger(EventRecordsResource.class);

    @Autowired
    private EventService eventService;

    /**
     * GET  /event-records?emailId=value get the "id" eventRecords.
     *
     * @param emailId the id of the eventRecords to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventRecords, or with status 404 (Not Found)
     */
    @GetMapping("/event-records")
    @Timed
    public ResponseEntity<EventRecords> getEventRecords(@RequestParam String emailId) {
        logger.debug("REST request to get EventRecords : {}", emailId);
        return eventService.getEventsForEmployee(emailId);
    }
}
