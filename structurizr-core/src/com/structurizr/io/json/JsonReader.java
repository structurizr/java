package com.structurizr.io.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.Workspace;
import com.structurizr.io.StructurizrReader;
import com.structurizr.io.StructurizrReaderException;

import java.io.IOException;
import java.io.Reader;

public class JsonReader implements StructurizrReader {

    public Workspace read(Reader reader) throws StructurizrReaderException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Workspace workspace = objectMapper.readValue(reader, Workspace.class);
            workspace.hydrate();

            return workspace;
        } catch (IOException ioe) {
            throw new StructurizrReaderException("Could not read JSON", ioe);
        }
    }

}