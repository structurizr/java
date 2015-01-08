package com.structurizr.io;

import com.structurizr.view.ViewSet;

import java.io.Writer;

public interface StructurizrWriter {

    void write(ViewSet viewSet, Writer writer) throws StructurizrWriterException;

}
