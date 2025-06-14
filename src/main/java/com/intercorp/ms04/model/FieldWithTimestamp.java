package com.intercorp.ms04.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldWithTimestamp {
    private String value;
    private ZonedDateTime timestamp;
}
