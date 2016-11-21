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

}
