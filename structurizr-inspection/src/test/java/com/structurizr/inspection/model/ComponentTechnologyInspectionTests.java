package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
ComponentTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Name");

        Violation violation = new ComponentTechnologyInspection(new DefaultInspector(workspace)).run(component);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.component.technology", violation.getType());
        assertEquals("The component \"Software System.Container.Name\" is missing a technology.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Name", "Description", "Technology");

        Violation violation = new ComponentTechnologyInspection(new DefaultInspector(workspace)).run(component);
        assertNull(violation);
    }

}
