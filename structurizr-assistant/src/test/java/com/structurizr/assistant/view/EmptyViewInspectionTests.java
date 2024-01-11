package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmptyViewInspectionTests {

    @Test
    public void run_EmptyView() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        Recommendation recommendation = new EmptyViewInspection(workspace).run(view);
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.views.view.empty", recommendation.getType());
        assertEquals("The view with key \"key\" is empty - add some elements.", recommendation.getDescription());
    }

    @Test
    public void run_NonEmptyView() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();

        Recommendation recommendation = new EmptyViewInspection(workspace).run(view);
        assertNull(recommendation);
    }

}
