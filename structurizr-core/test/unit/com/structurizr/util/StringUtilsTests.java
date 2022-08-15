package com.structurizr.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTests {

    @Test
    void test_isNullOrEmpty_ReturnsTrue_WhenPassedNull() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    void test_isNullOrEmpty_ReturnsTrue_WhenPassedAnEmptyString() {
        assertTrue(StringUtils.isNullOrEmpty(""));
        assertTrue(StringUtils.isNullOrEmpty(" "));
    }

    @Test
    void test_isNullOrEmpty_ReturnsFalse_WhenPassedANonEmptyString() {
        assertFalse(StringUtils.isNullOrEmpty("Hello World!"));
    }

}