package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ColorPairTests {

    @Test
    public void test_construction() {
        ColorPair colorPair = new ColorPair("#ffffff", "#000000");
        assertEquals("#ffffff", colorPair.getBackground());
        assertEquals("#000000", colorPair.getForeground());
    }

    @Test
    public void test_setBackground_WithAValidHtmlColorCode() {
        ColorPair colorPair = new ColorPair();
        colorPair.setBackground("#ffffff");
        assertEquals("#ffffff", colorPair.getBackground());
    }

    @Test
    public void test_setBackground_ThrowsAnException_WhenANullHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setBackground(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'null' is not a valid hex colour code.", iae.getMessage());
        }
    }

    @Test
    public void test_setBackground_ThrowsAnException_WhenAnEmptyHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setBackground("");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'' is not a valid hex colour code.", iae.getMessage());
        }
    }

    @Test
    public void test_setBackground_ThrowsAnException_WhenAnInvalidHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setBackground("ffffff");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'ffffff' is not a valid hex colour code.", iae.getMessage());
        }
    }

    @Test
    public void test_setForeground_WithAValidHtmlColorCode() {
        ColorPair colorPair = new ColorPair();
        colorPair.setForeground("#000000");
        assertEquals("#000000", colorPair.getForeground());
    }

    @Test
    public void test_setForeground_ThrowsAnException_WhenANullHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setForeground(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'null' is not a valid hex colour code.", iae.getMessage());
        }
    }

    @Test
    public void test_setForeground_ThrowsAnException_WhenAnEmptyHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setForeground("");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'' is not a valid hex colour code.", iae.getMessage());
        }
    }

    @Test
    public void test_setForeground_ThrowsAnException_WhenAnInvalidHtmlColorCodeIsSpecified() {
        try {
            ColorPair colorPair = new ColorPair();
            colorPair.setForeground("000000");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("'000000' is not a valid hex colour code.", iae.getMessage());
        }
    }

}
