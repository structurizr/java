package com.structurizr.io;

import com.structurizr.Workspace;

import java.io.Reader;

public interface WorkspaceReader {

    /**
     * Reads and parses a workspace definition.
     *
     * @param reader    a Reader on top of the workspace definition
     * @return          a Workspace object
     * @throws WorkspaceReaderException     if something goes wrong
     */
    Workspace read(Reader reader) throws WorkspaceReaderException;

}
