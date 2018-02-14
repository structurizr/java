package com.structurizr.view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FontTests {

    private Font font;

    @Before
    public void setUp() {
        this.font = new Font();
    }

    @Test
    public void construction_WithANameOnly() {
        this.font = new Font("Times New Roman");
        assertEquals("Times New Roman", font.getName());
    }

    @Test
    public void construction_WithANameAndUrl() {
        this.font = new Font("Open Sans", "https://fonts.googleapis.com/css?family=Open+Sans:400,700");
        assertEquals("Open Sans", font.getName());
        assertEquals("https://fonts.googleapis.com/css?family=Open+Sans:400,700", font.getUrl());
    }

    @Test
    public void test_setUrl_WithAUrl() {
        font.setUrl("https://fonts.googleapis.com/css?family=Open+Sans:400,700");
        assertEquals("https://fonts.googleapis.com/css?family=Open+Sans:400,700", font.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        font.setUrl("htt://blah");
    }

    @Test
    public void test_setUrl_DoesNothing_WhenANullUrlIsSpecified() {
        font.setUrl(null);
        assertNull(font.getUrl());
    }

    @Test
    public void test_setUrl_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        font.setUrl(" ");
        assertNull(font.getUrl());
    }

}
