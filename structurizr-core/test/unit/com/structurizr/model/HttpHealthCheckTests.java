package com.structurizr.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HttpHealthCheckTests {

    private HttpHealthCheck healthCheck;

    @Test
    void test_defaultConstructorExists() {
        // the default constructor is used when deserializing from JSON
        healthCheck = new HttpHealthCheck();
    }

    @Test
    void test_construction() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost", 120, 1000);
        assertEquals("Name", healthCheck.getName());
        assertEquals("http://localhost", healthCheck.getUrl());
        assertEquals(120, healthCheck.getInterval());
        assertEquals(1000, healthCheck.getTimeout());
    }

    @Test
    void test_addHeader() {
        healthCheck = new HttpHealthCheck();
        healthCheck.addHeader("Name", "Value");
        assertEquals("Value", healthCheck.getHeaders().get("Name"));
    }

    @Test
    void test_addHeader_ThrowsAnException_WhenTheHeaderNameIsNull() {
        healthCheck = new HttpHealthCheck();
        try {
            healthCheck.addHeader(null, "value");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void test_addHeader_ThrowsAnException_WhenTheHeaderNameIsEmpty() {
        healthCheck = new HttpHealthCheck();
        try {
            healthCheck.addHeader(" ", "value");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void test_addHeader_ThrowsAnException_WhenTheHeaderValueIsNull() {
        healthCheck = new HttpHealthCheck();
        try {
            healthCheck.addHeader("Name", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header value must not be null.", iae.getMessage());
        }
    }

    @Test
    void test_addHeader_DoesNotThrowAnException_WhenTheHeaderValueIsEmpty() {
        healthCheck = new HttpHealthCheck();
        healthCheck.addHeader("Name", "");
        assertEquals("", healthCheck.getHeaders().get("Name"));
    }

}