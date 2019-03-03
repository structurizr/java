package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiResponseTests {

    @Test
    public void test_parse_createsAnApiErrorObjectWithTheSpecifiedErrorMessage() throws Exception {
        ApiResponse apiResponse = ApiResponse.parse("{\"message\": \"Hello\"}");
        assertEquals("Hello", apiResponse.getMessage());
    }

}
