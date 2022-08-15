package com.structurizr.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiResponseTests {

    @Test
    void test_parse_createsAnApiErrorObjectWithTheSpecifiedErrorMessage() throws Exception {
        ApiResponse apiResponse = ApiResponse.parse("{\"message\": \"Hello\"}");
        assertEquals("Hello", apiResponse.getMessage());
    }

}
