package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
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

        Violation violation = new RelationshipTechnologyInspection(workspace).run(relationship);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.relationship.technology", violation.getType());
        assertEquals("Add a technology to the relationship between the software system named \"A\" and the software system named \"B\".", violation.getMessage());
    }

    @Test
    public void run_WithoutDescription_MediumPriority() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container a = softwareSystem.addContainer("A");
        Container b = softwareSystem.addContainer("B");
        Relationship relationship = a.uses(b, "Description");

        Violation violation = new RelationshipTechnologyInspection(workspace).run(relationship);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.relationship.technology", violation.getType());
        assertEquals("Add a technology to the relationship between the container named \"A\" and the container named \"B\".", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description", "Technology");

        Violation violation = new RelationshipTechnologyInspection(workspace).run(relationship);
        assertNull(violation);
    }

}
