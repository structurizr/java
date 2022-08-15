package com.structurizr.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HmacAuthorizationHeaderTests {

    private HmacAuthorizationHeader header;

    @Test
    void test_format() {
        header = new HmacAuthorizationHeader("apiKey", "f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8");
        assertEquals("apiKey:ZjdiYzgzZjQzMDUzODQyNGIxMzI5OGU2YWE2ZmIxNDNlZjRkNTlhMTQ5NDYxNzU5OTc0NzlkYmMyZDFhM2NkOA==", header.format());
    }

    @Test
    void test_parse() {
        header = HmacAuthorizationHeader.parse("apiKey:ZjdiYzgzZjQzMDUzODQyNGIxMzI5OGU2YWE2ZmIxNDNlZjRkNTlhMTQ5NDYxNzU5OTc0NzlkYmMyZDFhM2NkOA==");
        assertEquals("apiKey", header.getApiKey());
        assertEquals("f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8", header.getHmac());
    }

}