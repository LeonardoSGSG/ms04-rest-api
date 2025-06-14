package com.intercorp.ms04.model;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ConsolidatedDataResponse {
    private List<Person> person;
    private Map<String, FieldWithTimestamp> kafkaFields;
    private ZonedDateTime lastUpdate;
}
