package com.eleks.academy.pharmagator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonWriter {

    public static String write(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(o);
    }
}
