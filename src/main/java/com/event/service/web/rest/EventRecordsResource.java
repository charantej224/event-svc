package com.event.service.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.event.service.domain.EventRecords;
import com.event.service.repository.EventRecordsRepository;
import com.event.service.web.rest.errors.BadRequestAlertException;
import com.event.service.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EventRecords.
 */
@RestController
@RequestMapping("/api")
public class EventRecordsResource {

    private final Logger log = LoggerFactory.getLogger(EventRecordsResource.class);

    private static final String ENTITY_NAME = "eventServiceEventRecords";

    private final EventRecordsRepository eventRecordsRepository;

    public EventRecordsResource(EventRecordsRepository eventRecordsRepository) {
        this.eventRecordsRepository = eventRecordsRepository;
    }

    /**
     * POST  /event-records : Create a new eventRecords.
     *
     * @param eventRecords the eventRecords to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventRecords, or with status 400 (Bad Request) if the eventRecords has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-records")
    @Timed
    public ResponseEntity<EventRecords> createEventRecords(@RequestBody EventRecords eventRecords) throws URISyntaxException {
        log.debug("REST request to save EventRecords : {}", eventRecords);
        if (eventRecords.getId() != null) {
            throw new BadRequestAlertException("A new eventRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventRecords result = eventRecordsRepository.save(eventRecords);
        return ResponseEntity.created(new URI("/api/event-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-records : Updates an existing eventRecords.
     *
     * @param eventRecords the eventRecords to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventRecords,
     * or with status 400 (Bad Request) if the eventRecords is not valid,
     * or with status 500 (Internal Server Error) if the eventRecords couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-records")
    @Timed
    public ResponseEntity<EventRecords> updateEventRecords(@RequestBody EventRecords eventRecords) throws URISyntaxException {
        log.debug("REST request to update EventRecords : {}", eventRecords);
        if (eventRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventRecords result = eventRecordsRepository.save(eventRecords);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eventRecords.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-records : get all the eventRecords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eventRecords in body
     */
    @GetMapping("/event-records")
    @Timed
    public List<EventRecords> getAllEventRecords() {
        log.debug("REST request to get all EventRecords");
        return eventRecordsRepository.findAll();
    }

    /**
     * GET  /event-records/:id : get the "id" eventRecords.
     *
     * @param id the id of the eventRecords to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventRecords, or with status 404 (Not Found)
     */
    @GetMapping("/event-records/{id}")
    @Timed
    public ResponseEntity<EventRecords> getEventRecords(@PathVariable Long id) {
        log.debug("REST request to get EventRecords : {}", id);
        Optional<EventRecords> eventRecords = eventRecordsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eventRecords);
    }

    /**
     * DELETE  /event-records/:id : delete the "id" eventRecords.
     *
     * @param id the id of the eventRecords to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventRecords(@PathVariable Long id) {
        log.debug("REST request to delete EventRecords : {}", id);

        eventRecordsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
