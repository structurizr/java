package com.structurizr.io;

import com.structurizr.Workspace;

import java.io.Writer;

public interface WorkspaceWriter {

    /**
     * Writes a workspace definition to the specified Writer object.
     *
     * @param workspace     the Workspace object to write
     * @param writer        the Writer object to write the workspace to
     * @throws WorkspaceWriterException     if something goes wrong
     */
    void write(Workspace workspace, Writer writer) throws WorkspaceWriterException;

}
