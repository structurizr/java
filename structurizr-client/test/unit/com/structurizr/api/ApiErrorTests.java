package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiErrorTests {

    @Test
    public void test_parse_createsAnApiErrorObjectWithTheSpecifiedErrorMessage() throws Exception {
        ApiError apiError = ApiError.parse("{\"message\": \"Hello\"}");
        assertEquals("Hello", apiError.getMessage());
    }

}
