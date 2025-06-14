package com.intercorp.ms04.model;

import lombok.Data;

import java.util.Map;

@Data
public class KafkaMessageWrapper {
    private Map<String, FieldWithTimestamp> data;
}