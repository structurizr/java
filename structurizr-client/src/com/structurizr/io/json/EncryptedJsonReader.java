package com.structurizr.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.encryption.EncryptedWorkspace;
import com.structurizr.io.WorkspaceReaderException;

import java.io.IOException;
import java.io.Reader;

public final class EncryptedJsonReader extends AbstractJsonReader {

    public EncryptedJsonReader() {
    }

    /**
     * Reads and parses a workspace definition from a JSON document.
     *
     * @param reader    a Reader on top of the workspace definition
     * @return          a Workspace object
     * @throws WorkspaceReaderException     if something goes wrong
     */
    public EncryptedWorkspace read(Reader reader) throws WorkspaceReaderException {
        try {
            ObjectMapper objectMapper = createObjectMapper();

            return objectMapper.readValue(reader, EncryptedWorkspace.class);
        } catch (IOException ioe) {
            throw new WorkspaceReaderException("Could not read JSON", ioe);
        }
    }

}