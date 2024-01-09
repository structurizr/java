package com.structurizr.importer.documentation;

public class DocumentationImportException extends RuntimeException {

    public DocumentationImportException(Throwable cause) {
        super(cause);
    }

    public DocumentationImportException(String message, Throwable cause) {
        super(message, cause);
    }

}