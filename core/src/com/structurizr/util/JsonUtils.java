package com.structurizr.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.model.Model;

public class JsonUtils {

    public static String toJson(Model model, boolean indentOutput) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        if (indentOutput) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper.writeValueAsString(model);
    }

    public static Model toModel(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Model model = objectMapper.readValue(json, Model.class);
        model.hydrate();

        return model;
    }

}
