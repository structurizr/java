package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
ContainerTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name");

        Violation violation = new ContainerTechnologyInspection(new DefaultInspector(workspace)).run(container);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.container.technology", violation.getType());
        assertEquals("The container \"Software System.Name\" is missing a technology.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        Violation violation = new ContainerTechnologyInspection(new DefaultInspector(workspace)).run(container);
        assertNull(violation);
    }

}
