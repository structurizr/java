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
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "");

        Violation violation = new RelationshipDescriptionInspection(new DefaultInspector(workspace)).run(relationship);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.relationship.description", violation.getType());
        assertEquals("The relationship between the software system \"A\" and the software system \"B\" is missing a description.", violation.getMessage());
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
