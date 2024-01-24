package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
EmptyViewInspectionTests {

    @Test
    public void run_EmptyView() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        Violation violation = new EmptyViewInspection(new DefaultInspector(workspace)).run(view);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.view.empty", violation.getType());
        assertEquals("The view with key \"key\" is empty.", violation.getMessage());
    }

    @Test
    public void run_NonEmptyView() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();

        Violation violation = new EmptyViewInspection(new DefaultInspector(workspace)).run(view);
        assertNull(violation);
    }

}
