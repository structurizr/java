package com.structurizr.api;

/**
 * Thrown by the StructurizrClient when something goes wrong.
 */
public final class StructurizrClientException extends Exception {

    private static final long serialVersionUID = 1L;

    StructurizrClientException(String message) {
        super(message);
    }

    StructurizrClientException(Throwable cause) {
        super(cause);
    }

}
