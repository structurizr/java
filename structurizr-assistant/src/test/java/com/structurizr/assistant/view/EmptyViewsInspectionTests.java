package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmptyViewsInspectionTests {

    @Test
    public void run_WhenThereAreNoViews() {
        Workspace workspace = new Workspace("Name", "Description");

        Recommendation recommendation = new EmptyViewsInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.views.empty", recommendation.getType());
        assertEquals("Add some views to the workspace.", recommendation.getDescription());
    }

    @Test
    public void run_WhenThereAreViews() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().createSystemLandscapeView("key", "Description");

        Recommendation recommendation = new EmptyViewsInspection(workspace).run();
        assertNull(recommendation);
    }

}
