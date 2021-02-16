package com.structurizr.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceReader;
import com.structurizr.io.WorkspaceReaderException;
import com.structurizr.model.IdGenerator;
import com.structurizr.model.SequentialIntegerIdGeneratorStrategy;

import java.io.IOException;
import java.io.Reader;

/**
 * Reads a workspace definition as JSON.
 */
public final class JsonReader extends AbstractJsonReader implements WorkspaceReader {

    private IdGenerator idGenerator = null;

    /**
     * Sets the ID generator to use when parsing a JSON workspace definition.
     *
     * @param idGenerator   an IdGenerator implementation
     */
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Reads and parses a workspace definition from a JSON document.
     *
     * @param reader    a Reader on top of the workspace definition
     * @return          a Workspace object
     * @throws WorkspaceReaderException     if something goes wrong
     */
    public Workspace read(Reader reader) throws WorkspaceReaderException {
        try {
            ObjectMapper objectMapper = createObjectMapper();

            Workspace workspace = objectMapper.readValue(reader, Workspace.class);

            if (idGenerator != null) {
                workspace.getModel().setIdGenerator(idGenerator);
            }

            workspace.hydrate();

            return workspace;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new WorkspaceReaderException("Could not read JSON", ioe);
        }
    }

}