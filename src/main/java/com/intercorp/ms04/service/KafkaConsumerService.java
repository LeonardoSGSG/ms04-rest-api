package com.intercorp.ms04.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intercorp.ms04.model.FieldWithTimestamp;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Getter
    private final List<Map<String, FieldWithTimestamp>> kafkaData = new ArrayList<>();

    @KafkaListener(topics = "kafka-topic-02", groupId = "ms04-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            Map<String, FieldWithTimestamp> data = objectMapper.readValue(json, new TypeReference<>() {});
            kafkaData.add(data);
            log.info("Mensaje recibido y almacenado en memoria: {}", data);
        } catch (Exception e) {
            log.error("Error al procesar mensaje de Kafka-topic-02: ", e);
        }
    }
}