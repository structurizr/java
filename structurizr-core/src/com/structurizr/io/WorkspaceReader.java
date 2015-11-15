package com.structurizr.io;

import com.structurizr.Workspace;

import java.io.Reader;

public interface WorkspaceReader {

    Workspace read(Reader reader) throws WorkspaceReaderException;

}
