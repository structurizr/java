package com.structurizr.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlTests {

    @Test
    void test_isUrl_ReturnsFalse_WhenPassedNull() {
        assertFalse(Url.isUrl(null));
    }

    @Test
    void test_isUrl_ReturnsFalse_WhenPassedAnEmptyString() {
        assertFalse(Url.isUrl(""));
        assertFalse(Url.isUrl(" "));
    }

    @Test
    void test_isUrl_ReturnsFalse_WhenPassedAnInvalidUrl() {
        assertFalse(Url.isUrl("www.google.com"));
    }

    @Test
    void test_isUrl_ReturnsTrue_WhenPassedAValidUrl() {
        assertTrue(Url.isUrl("https://www.google.com"));
    }

}
