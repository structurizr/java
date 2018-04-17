package com.structurizr.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTests {

    @Test
    public void test_isNullOrEmpty_ReturnsTrue_WhenPassedNull() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    public void test_isNullOrEmpty_ReturnsTrue_WhenPassedAnEmptyString() {
        assertTrue(StringUtils.isNullOrEmpty(""));
        assertTrue(StringUtils.isNullOrEmpty(" "));
    }

    @Test
    public void test_isNullOrEmpty_ReturnsFalse_WhenPassedANonEmptyString() {
        assertFalse(StringUtils.isNullOrEmpty("Hello World!"));
    }

}