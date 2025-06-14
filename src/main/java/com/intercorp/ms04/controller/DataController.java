package com.intercorp.ms04.controller;

import com.intercorp.ms04.model.ConsolidatedDataResponse;
import com.intercorp.ms04.model.FieldWithTimestamp;
import com.intercorp.ms04.model.Person;
import com.intercorp.ms04.repository.PersonRepository;
import com.intercorp.ms04.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/consolidated")
    public ConsolidatedDataResponse getConsolidatedData() {
        List<Map<String, FieldWithTimestamp>> kafkaData = kafkaConsumerService.getKafkaData();
        List<Person> dbData = personRepository.findAll();

        Map<String, FieldWithTimestamp> mergedKafkaFields = kafkaData.stream()
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> v2
            ));

        ZonedDateTime lastUpdate = kafkaData.stream()
            .flatMap(map -> map.values().stream())
            .map(FieldWithTimestamp::getTimestamp)
            .max(Comparator.naturalOrder())
            .orElse(null);

        ConsolidatedDataResponse response = new ConsolidatedDataResponse();
        response.setLastUpdate(lastUpdate);
        response.setKafkaFields(mergedKafkaFields);
        response.setPerson(dbData);

        System.out.println("Respuesta para el front : " + response);

        return response;
    }

    @GetMapping("/db")
    public List<Person> getDbData() {
        return personRepository.findAll();
    }
}
