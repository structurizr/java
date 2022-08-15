package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorTests {

    @Test
    void isHexColorCode_ReturnsFalse_WhenPassedNull() {
        assertFalse(Color.isHexColorCode(null));
    }

    @Test
    void isHexColorCode_ReturnsFalse_WhenPassedAnEmptyString() {
        assertFalse(Color.isHexColorCode(""));
    }

    @Test
    void isHexColorCode_ReturnsFalse_WhenPassedAnInvalidString() {
        assertFalse(Color.isHexColorCode("ffffff"));
        assertFalse(Color.isHexColorCode("#fffff"));
        assertFalse(Color.isHexColorCode("#gggggg"));
    }

    @Test
    void isHexColorCode_ReturnsTrue_WhenPassedAnValidString() {
        assertTrue(Color.isHexColorCode("#abcdef"));
        assertTrue(Color.isHexColorCode("#ABCDEF"));
        assertTrue(Color.isHexColorCode("#123456"));
    }

}