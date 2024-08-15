package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RelationshipViewTests extends AbstractWorkspaceTestBase {

    @Test
    void getProperties_ReturnsAnEmptyMap_WhenNoPropertiesHaveBeenAdded() {
        RelationshipView relationshipView = new RelationshipView();
        assertEquals(0, relationshipView.getProperties().size());
    }

    @Test
    void getProperties_ReturnsAnUnmodifiableMap() {
        RelationshipView relationshipView = new RelationshipView();
        try {
            relationshipView.getProperties().put("name", "value");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsNull() {
        try {
            RelationshipView relationshipView = new RelationshipView();
            relationshipView.addProperty(null, "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            RelationshipView relationshipView = new RelationshipView();
            relationshipView.addProperty(" ", "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsNull() {
        try {
            RelationshipView relationshipView = new RelationshipView();
            relationshipView.addProperty("name", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsEmpty() {
        try {
            RelationshipView relationshipView = new RelationshipView();
            relationshipView.addProperty("name", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_AddsTheProperty_WhenANameAndValueAreSpecified() {
        RelationshipView relationshipView = new RelationshipView();
        relationshipView.addProperty("name", "value");
        assertEquals("value", relationshipView.getProperties().get("name"));
    }

    @Test
    void setProperties_DoesNothing_WhenNullIsSpecified() {
        RelationshipView relationshipView = new RelationshipView();
        relationshipView.setProperties(null);
        assertEquals(0, relationshipView.getProperties().size());
    }

    @Test
    void setProperties_SetsTheProperties_WhenANonEmptyMapIsSpecified() {
        RelationshipView relationshipView = new RelationshipView();
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "value");
        relationshipView.setProperties(properties);
        assertEquals(1, relationshipView.getProperties().size());
        assertEquals("value", relationshipView.getProperties().get("name"));
    }

}