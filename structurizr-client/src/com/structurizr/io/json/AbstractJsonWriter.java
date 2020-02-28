package com.structurizr.io.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

class AbstractJsonWriter {

    private static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    ObjectMapper createObjectMapper(boolean indentOutput) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (indentOutput) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        objectMapper.setDateFormat(new SimpleDateFormat(AbstractJsonWriter.ISO_DATE_TIME_FORMAT));

        return objectMapper;
    }

}