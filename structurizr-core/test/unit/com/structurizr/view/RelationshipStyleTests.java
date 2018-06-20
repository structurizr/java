package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RelationshipStyleTests {

    private RelationshipStyle relationshipStyle = new RelationshipStyle("tag");

    @Test
    public void test_setPosition_SetsPositionToNull_WhenNullIsSpecified() {
        relationshipStyle.setPosition(null);
        assertNull(relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPositionToZero_WhenANegativeNumberIsSpecified() {
        relationshipStyle.setPosition(-1);
        assertEquals(new Integer(0), relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPositionToOneHundred_WhenANumberGreaterThanOneHundredIsSpecified() {
        relationshipStyle.setPosition(101);
        assertEquals(new Integer(100), relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPosition_WhenANumberBetweenZeroAndOneHundredIsSpecified() {
        relationshipStyle.setPosition(0);
        assertEquals(new Integer(0), relationshipStyle.getPosition());

        relationshipStyle.setPosition(1);
        assertEquals(new Integer(1), relationshipStyle.getPosition());

        relationshipStyle.setPosition(50);
        assertEquals(new Integer(50), relationshipStyle.getPosition());


        relationshipStyle.setPosition(99);
        assertEquals(new Integer(99), relationshipStyle.getPosition());

        relationshipStyle.setPosition(100);
        assertEquals(new Integer(100), relationshipStyle.getPosition());
    }

    @Test
    public void test_setOpacity() {
        RelationshipStyle style = new RelationshipStyle();
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
        RelationshipStyle style = new RelationshipStyle();
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
        RelationshipStyle style = new RelationshipStyle();
        style.setColor("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.setColor("#FFFFFF");
        assertEquals("#ffffff", style.getColor());

        style.setColor("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test
    public void test_color_SetsTheColorProperty_WhenAValidHexColorCodeIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.color("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.color("#FFFFFF");
        assertEquals("#ffffff", style.getColor());

        style.color("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setColor_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.setColor("white");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_color_ThrowsAnException_WhenAnInvalidHexColorCodeIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.color("white");
    }

}
