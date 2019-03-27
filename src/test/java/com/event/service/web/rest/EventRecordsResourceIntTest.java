package com.event.service.web.rest;

import com.event.service.EventServiceApp;

import com.event.service.domain.EventRecords;
import com.event.service.repository.EventRecordsRepository;
import com.event.service.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.event.service.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventRecordsResource REST controller.
 *
 * @see EventRecordsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventServiceApp.class)
public class EventRecordsResourceIntTest {

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EventRecordsRepository eventRecordsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventRecordsMockMvc;

    private EventRecords eventRecords;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventRecordsResource eventRecordsResource = new EventRecordsResource(eventRecordsRepository);
        this.restEventRecordsMockMvc = MockMvcBuilders.standaloneSetup(eventRecordsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventRecords createEntity(EntityManager em) {
        EventRecords eventRecords = new EventRecords()
            .emailId(DEFAULT_EMAIL_ID)
            .eventName(DEFAULT_EVENT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return eventRecords;
    }

    @Before
    public void initTest() {
        eventRecords = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventRecords() throws Exception {
        int databaseSizeBeforeCreate = eventRecordsRepository.findAll().size();

        // Create the EventRecords
        restEventRecordsMockMvc.perform(post("/api/event-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventRecords)))
            .andExpect(status().isCreated());

        // Validate the EventRecords in the database
        List<EventRecords> eventRecordsList = eventRecordsRepository.findAll();
        assertThat(eventRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        EventRecords testEventRecords = eventRecordsList.get(eventRecordsList.size() - 1);
        assertThat(testEventRecords.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testEventRecords.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEventRecords.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventRecords.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createEventRecordsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRecordsRepository.findAll().size();

        // Create the EventRecords with an existing ID
        eventRecords.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventRecordsMockMvc.perform(post("/api/event-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventRecords)))
            .andExpect(status().isBadRequest());

        // Validate the EventRecords in the database
        List<EventRecords> eventRecordsList = eventRecordsRepository.findAll();
        assertThat(eventRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventRecords() throws Exception {
        // Initialize the database
        eventRecordsRepository.saveAndFlush(eventRecords);

        // Get all the eventRecordsList
        restEventRecordsMockMvc.perform(get("/api/event-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID.toString())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEventRecords() throws Exception {
        // Initialize the database
        eventRecordsRepository.saveAndFlush(eventRecords);

        // Get the eventRecords
        restEventRecordsMockMvc.perform(get("/api/event-records/{id}", eventRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventRecords.getId().intValue()))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID.toString()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventRecords() throws Exception {
        // Get the eventRecords
        restEventRecordsMockMvc.perform(get("/api/event-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventRecords() throws Exception {
        // Initialize the database
        eventRecordsRepository.saveAndFlush(eventRecords);

        int databaseSizeBeforeUpdate = eventRecordsRepository.findAll().size();

        // Update the eventRecords
        EventRecords updatedEventRecords = eventRecordsRepository.findById(eventRecords.getId()).get();
        // Disconnect from session so that the updates on updatedEventRecords are not directly saved in db
        em.detach(updatedEventRecords);
        updatedEventRecords
            .emailId(UPDATED_EMAIL_ID)
            .eventName(UPDATED_EVENT_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restEventRecordsMockMvc.perform(put("/api/event-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventRecords)))
            .andExpect(status().isOk());

        // Validate the EventRecords in the database
        List<EventRecords> eventRecordsList = eventRecordsRepository.findAll();
        assertThat(eventRecordsList).hasSize(databaseSizeBeforeUpdate);
        EventRecords testEventRecords = eventRecordsList.get(eventRecordsList.size() - 1);
        assertThat(testEventRecords.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testEventRecords.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEventRecords.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventRecords.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEventRecords() throws Exception {
        int databaseSizeBeforeUpdate = eventRecordsRepository.findAll().size();

        // Create the EventRecords

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventRecordsMockMvc.perform(put("/api/event-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventRecords)))
            .andExpect(status().isBadRequest());

        // Validate the EventRecords in the database
        List<EventRecords> eventRecordsList = eventRecordsRepository.findAll();
        assertThat(eventRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventRecords() throws Exception {
        // Initialize the database
        eventRecordsRepository.saveAndFlush(eventRecords);

        int databaseSizeBeforeDelete = eventRecordsRepository.findAll().size();

        // Get the eventRecords
        restEventRecordsMockMvc.perform(delete("/api/event-records/{id}", eventRecords.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventRecords> eventRecordsList = eventRecordsRepository.findAll();
        assertThat(eventRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventRecords.class);
        EventRecords eventRecords1 = new EventRecords();
        eventRecords1.setId(1L);
        EventRecords eventRecords2 = new EventRecords();
        eventRecords2.setId(eventRecords1.getId());
        assertThat(eventRecords1).isEqualTo(eventRecords2);
        eventRecords2.setId(2L);
        assertThat(eventRecords1).isNotEqualTo(eventRecords2);
        eventRecords1.setId(null);
        assertThat(eventRecords1).isNotEqualTo(eventRecords2);
    }
}
