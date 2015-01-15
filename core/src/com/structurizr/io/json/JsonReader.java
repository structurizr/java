package com.structurizr.io.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.io.StructurizrReader;
import com.structurizr.io.StructurizrReaderException;
import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

import java.io.IOException;
import java.io.Reader;

public class JsonReader implements StructurizrReader {

    public ViewSet read(Reader reader) throws StructurizrReaderException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            ModelAndViewSet modelAndViewSet = objectMapper.readValue(reader, ModelAndViewSet.class);
            modelAndViewSet.getViews().setModel(modelAndViewSet.getModel());
            modelAndViewSet.getModel().hydrate();
            modelAndViewSet.getViews().hydrate();

            return modelAndViewSet.getViews();
        } catch (IOException ioe) {
            throw new StructurizrReaderException("Could not read JSON", ioe);
        }
    }

}