package com.structurizr.io;

import com.structurizr.Workspace;

import java.io.Reader;

public interface StructurizrReader {

    Workspace read(Reader reader) throws StructurizrReaderException;

}
