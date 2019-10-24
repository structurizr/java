package com.structurizr.io.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.Workspace;
import com.structurizr.WorkspaceValidationException;
import com.structurizr.io.WorkspaceReader;
import com.structurizr.io.WorkspaceReaderException;

import java.io.IOException;
import java.io.Reader;

/**
 * Reads a workspace definition as JSON.
 */
public final class JsonReader implements WorkspaceReader {

    /**
     * Reads and parses a workspace definition from a JSON document.
     *
     * @param reader    a Reader on top of the workspace definition
     * @return          a Workspace object
     * @throws WorkspaceReaderException     if something goes wrong
     */
    public Workspace read(Reader reader) throws WorkspaceReaderException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Workspace workspace = objectMapper.readValue(reader, Workspace.class);
            workspace.hydrate();

            return workspace;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new WorkspaceReaderException("Could not read JSON", ioe);
        }
    }

}