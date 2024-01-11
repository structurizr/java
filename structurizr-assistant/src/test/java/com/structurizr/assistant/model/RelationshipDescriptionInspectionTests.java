package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RelationshipDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription_MediumPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "");

        Recommendation recommendation = new RelationshipDescriptionInspection(workspace).run(relationship);
        Assertions.assertEquals(Recommendation.Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.relationship.description", recommendation.getType());
        assertEquals("Add a description to the relationship between the software system named \"A\" and the software system named \"B\".", recommendation.getDescription());
    }

    @Test
    public void run_WithoutDescription_LowPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component a = container.addComponent("A");
        Component b = container.addComponent("B");
        Relationship relationship = a.uses(b, "");

        Recommendation recommendation = new RelationshipDescriptionInspection(workspace).run(relationship);
        Assertions.assertEquals(Recommendation.Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.relationship.description", recommendation.getType());
        assertEquals("Add a description to the relationship between the component named \"A\" and the component named \"B\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description");

        Recommendation recommendation = new RelationshipDescriptionInspection(workspace).run(relationship);
        assertNull(recommendation);
    }

}
