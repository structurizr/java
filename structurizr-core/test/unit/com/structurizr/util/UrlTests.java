package com.structurizr.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UrlTests {

    @Test
    public void test_isUrl_ReturnsFalse_WhenPassedNull() {
        assertFalse(Url.isUrl(null));
    }

    @Test
    public void test_isUrl_ReturnsFalse_WhenPassedAnEmptyString() {
        assertFalse(Url.isUrl(""));
        assertFalse(Url.isUrl(" "));
    }

    @Test
    public void test_isUrl_ReturnsFalse_WhenPassedAnInvalidUrl() {
        assertFalse(Url.isUrl("www.google.com"));
    }

    @Test
    public void test_isUrl_ReturnsTrue_WhenPassedAValidUrl() {
        assertTrue(Url.isUrl("https://www.google.com"));
    }

}
