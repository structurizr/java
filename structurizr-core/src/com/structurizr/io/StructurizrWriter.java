package com.structurizr.io;

import com.structurizr.Workspace;

import java.io.Writer;

public interface StructurizrWriter {

    void write(Workspace workspace, Writer writer) throws StructurizrWriterException;

}
