package com.structurizr.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FontTests {

    private Font font;

    @BeforeEach
    public void setUp() {
        this.font = new Font();
    }

    @Test
    void construction_WithANameOnly() {
        this.font = new Font("Times New Roman");
        assertEquals("Times New Roman", font.getName());
    }

    @Test
    void construction_WithANameAndUrl() {
        this.font = new Font("Open Sans", "https://fonts.googleapis.com/css?family=Open+Sans:400,700");
        assertEquals("Open Sans", font.getName());
        assertEquals("https://fonts.googleapis.com/css?family=Open+Sans:400,700", font.getUrl());
    }

    @Test
    void test_setUrl_WithAUrl() {
        font.setUrl("https://fonts.googleapis.com/css?family=Open+Sans:400,700");
        assertEquals("https://fonts.googleapis.com/css?family=Open+Sans:400,700", font.getUrl());
    }

    @Test
    void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            font.setUrl("htt://blah");
        });
    }

    @Test
    void test_setUrl_DoesNothing_WhenANullUrlIsSpecified() {
        font.setUrl(null);
        assertNull(font.getUrl());
    }

    @Test
    void test_setUrl_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        font.setUrl(" ");
        assertNull(font.getUrl());
    }

}
