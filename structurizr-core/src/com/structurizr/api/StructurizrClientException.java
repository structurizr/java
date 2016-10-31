package com.structurizr.api;

public final class StructurizrClientException extends Exception {

    private static final long serialVersionUID = 1L;

    StructurizrClientException(String message) {
        super(message);
    }

    StructurizrClientException(Throwable cause) {
        super(cause);
    }

}
