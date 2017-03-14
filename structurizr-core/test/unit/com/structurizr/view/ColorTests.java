package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ColorTests {

    @Test
    public void test_isHexColorCode_ReturnsFalse_WhenPassedNull() {
        assertFalse(Color.isHexColorCode(null));
    }

    @Test
    public void test_isHexColorCode_ReturnsFalse_WhenPassedAnEmptyString() {
        assertFalse(Color.isHexColorCode(""));
    }

    @Test
    public void test_isHexColorCode_ReturnsFalse_WhenPassedAnInvalidString() {
        assertFalse(Color.isHexColorCode("ffffff"));
        assertFalse(Color.isHexColorCode("#fffff"));
        assertFalse(Color.isHexColorCode("#gggggg"));
    }

    @Test
    public void test_isHexColorCode_ReturnsTrue_WhenPassedAnValidString() {
        assertTrue(Color.isHexColorCode("#abcdef"));
        assertTrue(Color.isHexColorCode("#ABCDEF"));
        assertTrue(Color.isHexColorCode("#123456"));
    }

}