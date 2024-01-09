package com.structurizr.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a response returned by the Structurizr API.
 */
final class ApiResponse {

    private boolean success;
    private String message;
    private Long revision;

    ApiResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public Long getRevision() {
        return revision;
    }

    void setRevision(Long revision) {
        this.revision = revision;
    }

    static ApiResponse parse(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, ApiResponse.class);
    }

}