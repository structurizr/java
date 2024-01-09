package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RelationshipTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription_LowPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description");

        Recommendation recommendation = new RelationshipTechnologyInspection(workspace).run(relationship);
        Assertions.assertEquals(Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.relationship.technology", recommendation.getType());
        assertEquals("Add a technology to the relationship between the software system named \"A\" and the software system named \"B\".", recommendation.getDescription());
    }

    @Test
    public void run_WithoutDescription_MediumPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container a = softwareSystem.addContainer("A");
        Container b = softwareSystem.addContainer("B");
        Relationship relationship = a.uses(b, "Description");

        Recommendation recommendation = new RelationshipTechnologyInspection(workspace).run(relationship);
        Assertions.assertEquals(Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.relationship.technology", recommendation.getType());
        assertEquals("Add a technology to the relationship between the container named \"A\" and the container named \"B\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description", "Technology");

        Recommendation recommendation = new RelationshipTechnologyInspection(workspace).run(relationship);
        assertNull(recommendation);
    }

}
