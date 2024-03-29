package com.structurizr.view;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RelationshipStyleTests {

    private RelationshipStyle relationshipStyle = new RelationshipStyle("tag");

    @Test
    void setPosition_SetsPositionToNull_WhenNullIsSpecified() {
        relationshipStyle.setPosition(null);
        assertNull(relationshipStyle.getPosition());
    }

    @Test
    void setPosition_SetsPositionToZero_WhenANegativeNumberIsSpecified() {
        relationshipStyle.setPosition(-1);
        assertEquals(Integer.valueOf(0), relationshipStyle.getPosition());
    }

    @Test
    void setPosition_SetsPositionToOneHundred_WhenANumberGreaterThanOneHundredIsSpecified() {
        relationshipStyle.setPosition(101);
        assertEquals(Integer.valueOf(100), relationshipStyle.getPosition());
    }

    @Test
    void setPosition_SetsPosition_WhenANumberBetweenZeroAndOneHundredIsSpecified() {
        relationshipStyle.setPosition(0);
        assertEquals(Integer.valueOf(0), relationshipStyle.getPosition());

        relationshipStyle.setPosition(1);
        assertEquals(Integer.valueOf(1), relationshipStyle.getPosition());

        relationshipStyle.setPosition(50);
        assertEquals(Integer.valueOf(50), relationshipStyle.getPosition());


        relationshipStyle.setPosition(99);
        assertEquals(Integer.valueOf(99), relationshipStyle.getPosition());

        relationshipStyle.setPosition(100);
        assertEquals(Integer.valueOf(100), relationshipStyle.getPosition());
    }

    @Test
    void setOpacity() {
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
    void opacity() {
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
    void setColor_SetsTheColorProperty_WhenAValidHexColorCodeIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.setColor("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.setColor("#FFFFFF");
        assertEquals("#ffffff", style.getColor());

        style.setColor("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test
    void color_SetsTheColorProperty_WhenAValidHexColorCodeIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.color("#ffffff");
        assertEquals("#ffffff", style.getColor());

        style.color("#FFFFFF");
        assertEquals("#ffffff", style.getColor());

        style.color("#123456");
        assertEquals("#123456", style.getColor());
    }

    @Test
    void setColor_SetsTheColorProperty_WhenAValidColorNameIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.setColor("yellow");
        assertEquals("#ffff00", style.getColor());
    }

    @Test
    void color_SetsTheColorProperty_WhenAValidColorNameIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.color("yellow");
        assertEquals("#ffff00", style.getColor());
    }

    @Test
    void setColor_ThrowsAnException_WhenAnInvalidColorIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelationshipStyle style = new RelationshipStyle();
            style.setColor("hello");
        });
    }

    @Test
    void color_ThrowsAnException_WhenAnInvalidColorIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelationshipStyle style = new RelationshipStyle();
            style.color("hello");
        });
    }

    @Test
    void getProperties_ReturnsAnEmptyList_WhenNoPropertiesHaveBeenAdded() {
        RelationshipStyle style = new RelationshipStyle();
        assertEquals(0, style.getProperties().size());
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsNull() {
        try {
            RelationshipStyle style = new RelationshipStyle();
            style.addProperty(null, "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            RelationshipStyle style = new RelationshipStyle();
            style.addProperty(" ", "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsNull() {
        try {
            RelationshipStyle style = new RelationshipStyle();
            style.addProperty("name", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsEmpty() {
        try {
            RelationshipStyle style = new RelationshipStyle();
            style.addProperty("name", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_AddsTheProperty_WhenANameAndValueAreSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.addProperty("name", "value");
        assertEquals("value", style.getProperties().get("name"));
    }

    @Test
    void setProperties_DoesNothing_WhenNullIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        style.setProperties(null);
        assertEquals(0, style.getProperties().size());
    }

    @Test
    void setProperties_SetsTheProperties_WhenANonEmptyMapIsSpecified() {
        RelationshipStyle style = new RelationshipStyle();
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "value");
        style.setProperties(properties);
        assertEquals(1, style.getProperties().size());
        assertEquals("value", style.getProperties().get("name"));
    }

}
