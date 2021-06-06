package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.*;

public class DimensionsTests {

    @Test
    public void test_construction() {
        Dimensions dimensions = new Dimensions(123, 456);

        assertEquals(123, dimensions.getWidth());
        assertEquals(456, dimensions.getHeight());
    }

    @Test
    public void test_setWidth_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The width must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_setHeight_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            Dimensions dimensions = new Dimensions();
            dimensions.setHeight(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The height must be a positive integer.", iae.getMessage());
        }
    }

}