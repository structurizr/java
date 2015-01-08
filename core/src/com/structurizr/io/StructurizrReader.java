package com.structurizr.io;

import com.structurizr.view.ViewSet;

import java.io.Reader;

public interface StructurizrReader {

    ViewSet read(Reader reader) throws StructurizrReaderException;

}
