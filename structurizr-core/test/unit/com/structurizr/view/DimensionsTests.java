package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DimensionsTests {

    @Test
    void construction() {
        Dimensions dimensions = new Dimensions(123, 456);

        assertEquals(123, dimensions.getWidth());
        assertEquals(456, dimensions.getHeight());
    }

    @Test
    void setWidth_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The width must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    void setHeight_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            Dimensions dimensions = new Dimensions();
            dimensions.setHeight(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The height must be a positive integer.", iae.getMessage());
        }
    }

}