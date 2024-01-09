package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ComponentTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Name");

        Recommendation recommendation = new ComponentTechnologyInspection(workspace).run(component);
        Assertions.assertEquals(Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.component.technology", recommendation.getType());
        assertEquals("Add a technology to the component named \"Name\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Name", "Description", "Technology");

        Recommendation recommendation = new ComponentTechnologyInspection(workspace).run(component);
        assertNull(recommendation);
    }

}
