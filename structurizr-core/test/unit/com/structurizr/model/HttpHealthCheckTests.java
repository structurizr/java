package com.structurizr.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HttpHealthCheckTests {

    private HttpHealthCheck healthCheck;

    @Test
    public void test_defaultConstructorExists() {
        // the default constructor is used when deserializing from JSON
        healthCheck = new HttpHealthCheck();
    }

    @Test
    public void test_construction() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        assertEquals("Name", healthCheck.getName());
        assertEquals("http://localhost", healthCheck.getUrl());
        assertEquals(60, healthCheck.getInterval());
        assertEquals(0, healthCheck.getTimeout());
    }

    @Test
    public void test_construction_ThrowsAnException_WhenTheNameIsNull() {
        try {
            healthCheck = new HttpHealthCheck(null, "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            healthCheck = new HttpHealthCheck(" ", "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenTheUrlIsNull() {
        try {
            healthCheck = new HttpHealthCheck("Name", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenTheUrlIsEmpty() {
        try {
            healthCheck = new HttpHealthCheck("Name", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenTheUrlIsInvalid() {
        try {
            healthCheck = new HttpHealthCheck("Name", "localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("localhost is not a valid URL.", iae.getMessage());
        }
    }

    @Test
    public void test_interval() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        healthCheck.setInterval(30);
        assertEquals(30, healthCheck.getInterval());
    }

    @Test
    public void test_setInterval_ThrowsAnException_WhenTheInterbalIsLessThanZero() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        try {
            healthCheck.setInterval(-1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The polling interval must be zero or a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_timeout() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        healthCheck.setTimeout(1000);
        assertEquals(1000, healthCheck.getTimeout());
    }

    @Test
    public void test_setTimeout_ThrowsAnException_WhenTheTimeoutIsLessThanZero() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        try {
            healthCheck.setTimeout(-1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The timeout must be zero or a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_addHeader() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        healthCheck.addHeader("Name", "Value");
        assertEquals("Value", healthCheck.getHeaders().get("Name"));
    }

    @Test
    public void test_addHeader_ThrowsAnException_WhenTheHeaderNameIsNull() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        try {
            healthCheck.addHeader(null, "value");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_addHeader_ThrowsAnException_WhenTheHeaderNameIsEmpty() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        try {
            healthCheck.addHeader(" ", "value");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_addHeader_ThrowsAnException_WhenTheHeaderValueIsNull() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        try {
            healthCheck.addHeader("Name", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The header value must not be null.", iae.getMessage());
        }
    }

    @Test
    public void test_addHeader_DoesNotThrowAnException_WhenTheHeaderValueIsEmpty() {
        healthCheck = new HttpHealthCheck("Name", "http://localhost");
        healthCheck.addHeader("Name", "");
        assertEquals("", healthCheck.getHeaders().get("Name"));
    }

}