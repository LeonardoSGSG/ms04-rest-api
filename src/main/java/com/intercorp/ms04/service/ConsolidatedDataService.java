package com.intercorp.ms04.service;

import com.intercorp.ms04.model.ConsolidatedDataResponse;
import org.springframework.stereotype.Service;

@Service
public class ConsolidatedDataService {

    public ConsolidatedDataResponse getConsolidatedData() {
        return new ConsolidatedDataResponse();
    }
}
