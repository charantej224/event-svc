package com.event.service.integration.tests;

import com.event.service.EventServiceApp;
import com.event.service.domain.EventRecords;
import com.event.service.repository.CustomEventRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@EnableJpaRepositories("com.event.service.repository")
@ComponentScan(basePackages = { "com.event.service.*" })
@EntityScan("com.event.service.*")
@SpringBootTest(classes = {EventServiceApp.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:config/application.yml")
public class EventResourceIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @LocalManagementPort
    int randomManagementPort;

    @Autowired
    private CustomEventRepository customEventRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup(){

        EventRecords eventRecord1 = new EventRecords();
        eventRecord1.setEmailId("test@email");
        eventRecord1.setEventName("CREATE");
        EventRecords eventRecord2 = new EventRecords();
        eventRecord2.setEmailId("test@email");
        eventRecord2.setEventName("UPDATE");
        customEventRepository.deleteAll();
        customEventRepository.save(eventRecord1);
        customEventRepository.save(eventRecord2);
    }

    @Test
    public void test_getEvents() throws Exception{
        RequestBuilder rb = MockMvcRequestBuilders.get("/api/event-records?emailId=test@email").accept(MediaType.APPLICATION_JSON);
        MvcResult result =  mockMvc.perform(rb).andReturn();
        String resultValue = result.getResponse().getContentAsString();
        Assert.assertTrue(resultValue.contains("UPDATE"));
        Assert.assertTrue(resultValue.contains("CREATE"));


    }
}
