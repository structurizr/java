package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SoftwareSystemDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");

        Recommendation recommendation = new SoftwareSystemDescriptionInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.softwaresystem.description", recommendation.getType());
        assertEquals("Add a description to the software system named \"Name\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        Recommendation recommendation = new SoftwareSystemDescriptionInspection(workspace).run(softwareSystem);
        assertNull(recommendation);
    }

}
