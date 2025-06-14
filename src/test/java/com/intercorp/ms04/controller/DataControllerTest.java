package com.intercorp.ms04.controller;

import com.intercorp.ms04.model.ConsolidatedDataResponse;
import com.intercorp.ms04.model.FieldWithTimestamp;
import com.intercorp.ms04.model.Person;
import com.intercorp.ms04.repository.PersonRepository;
import com.intercorp.ms04.service.KafkaConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataController.class)
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaConsumerService kafkaConsumerService;

    @MockBean
    private PersonRepository personRepository;

    @Test
    public void testGetConsolidatedData() throws Exception {

        Map<String, FieldWithTimestamp> kafkaMap = new HashMap<>();
        kafkaMap.put("exampleField", new FieldWithTimestamp("value", ZonedDateTime.now()));
        List<Map<String, FieldWithTimestamp>> kafkaData = List.of(kafkaMap);
        when(kafkaConsumerService.getKafkaData()).thenReturn(kafkaData);

        Person person = new Person();
        person.setFirstname("Leonardo");
        person.setLastname("Sipión");
        List<Person> dbData = List.of(person);
        when(personRepository.findAll()).thenReturn(dbData);

        mockMvc.perform(get("/api/data/consolidated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person[0].firstname").value("Leonardo"))
                .andExpect(jsonPath("$.kafkaFields.exampleField.value").value("value"))
                .andExpect(jsonPath("$.lastUpdate").exists());
    }
}
