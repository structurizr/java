package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ElementNotIncludedInAnyViewsInspectionTests {

    @Test
    public void run_NotInViews() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");

        Recommendation recommendation = new ElementNotIncludedInAnyViewsInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.element.noview", recommendation.getType());
        assertEquals("The software system named \"Name\" is not included on any views - add it to a view.", recommendation.getDescription());
    }

    @Test
    public void run_InViews() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().createSystemLandscapeView("key", "Description").addAllElements();

        Recommendation recommendation = new ElementNotIncludedInAnyViewsInspection(workspace).run(softwareSystem);
        assertNull(recommendation);
    }

}
