package com.structurizr.encryption;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.io.StructurizrReaderException;

import java.io.IOException;
import java.io.Reader;

public class EncryptedJsonReader {

    public EncryptedJsonReader() {
    }

    public EncryptedWorkspace read(Reader reader) throws StructurizrReaderException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper.readValue(reader, EncryptedWorkspace.class);
        } catch (IOException ioe) {
            throw new StructurizrReaderException("Could not read JSON", ioe);
        }
    }

}