package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SystemContextViewsForMultipleSoftwareSystemsInspectionTests {

    @Test
    public void run_MultipleSoftwareSystems() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemContextView(a, "SystemContext-A", "Description");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        workspace.getViews().createSystemContextView(b, "SystemContext-B", "Description");

        Recommendation recommendation = new SystemContextViewsForMultipleSoftwareSystemsInspection(workspace).run();
        Assertions.assertEquals(Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.scope", recommendation.getType());
        assertEquals("System context views exist for 2 software systems. It is recommended that a workspace includes system context views for a single software system only.", recommendation.getDescription());
    }

    @Test
    public void run_SingleSoftwareSystem() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemContextView(a, "SystemContext-A", "Description");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");

        Recommendation recommendation = new SystemContextViewsForMultipleSoftwareSystemsInspection(workspace).run();
        assertNull(recommendation);
    }

}
