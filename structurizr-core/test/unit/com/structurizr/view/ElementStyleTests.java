package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ElementStyleTests {

    @Test
    public void test_setOpacity() {
        ElementStyle style = new ElementStyle();
        assertNull(style.getOpacity());

        style.setOpacity(-1);
        assertEquals(0, style.getOpacity().intValue());

        style.setOpacity(0);
        assertEquals(0, style.getOpacity().intValue());

        style.setOpacity(50);
        assertEquals(50, style.getOpacity().intValue());

        style.setOpacity(100);
        assertEquals(100, style.getOpacity().intValue());

        style.setOpacity(101);
        assertEquals(100, style.getOpacity().intValue());
    }

    @Test
    public void test_opacity() {
        ElementStyle style = new ElementStyle();
        assertNull(style.getOpacity());

        style.opacity(-1);
        assertEquals(0, style.getOpacity().intValue());

        style.opacity(0);
        assertEquals(0, style.getOpacity().intValue());

        style.opacity(50);
        assertEquals(50, style.getOpacity().intValue());

        style.opacity(100);
        assertEquals(100, style.getOpacity().intValue());

        style.opacity(101);
        assertEquals(100, style.getOpacity().intValue());
    }

    @Test
    public void test_setColor_SetsTheColorProperty_WhenAValidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.setColor("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.setColor("#FFFFFF");
        assertEquals("#FFFFFF", style.getColor());

        style.setColor("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test
    public void test_color_SetsTheColorProperty_WhenAValidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.color("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.color("#FFFFFF");
        assertEquals("#FFFFFF", style.getColor());

        style.color("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setColor_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.setColor("white");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_color_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.color("white");
    }

    @Test
    public void test_setBackground_SetsTheBackgroundProperty_WhenAValidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.setBackground("#ffffff");
        assertEquals("#ffffff", style.getBackground());

        style.setBackground("#FFFFFF");
        assertEquals("#FFFFFF", style.getBackground());

        style.setBackground("#123456");
        assertEquals("#123456", style.getBackground());
    }

    @Test
    public void test_background_SetsTheBackgroundProperty_WhenAValidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.background("#ffffff");
        assertEquals("#ffffff", style.getBackground());

        style.background("#FFFFFF");
        assertEquals("#FFFFFF", style.getBackground());

        style.background("#123456");
        assertEquals("#123456", style.getBackground());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setBackground_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.setBackground("white");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_background_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        ElementStyle style = new ElementStyle();
        style.background("white");
    }

}
