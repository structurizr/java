package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
RelationshipDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription_MediumPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "");

        Violation violation = new RelationshipDescriptionInspection(new DefaultInspector(workspace)).run(relationship);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.relationship.description", violation.getType());
        assertEquals("Add a description to the relationship between the software system named \"A\" and the software system named \"B\".", violation.getMessage());
    }

    @Test
    public void run_WithoutDescription_LowPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component a = container.addComponent("A");
        Component b = container.addComponent("B");
        Relationship relationship = a.uses(b, "");

        Violation violation = new RelationshipDescriptionInspection(new DefaultInspector(workspace)).run(relationship);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.relationship.description", violation.getType());
        assertEquals("Add a description to the relationship between the component named \"A\" and the component named \"B\".", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description");

        Violation violation = new RelationshipDescriptionInspection(new DefaultInspector(workspace)).run(relationship);
        assertNull(violation);
    }

}
