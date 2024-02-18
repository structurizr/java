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

public class ManualLayoutInspectionTests {

    @Test
    public void run_GeneratedKeyAndManualLayout() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("", "Description");

        Violation violation = new ManualLayoutInspection(new DefaultInspector(workspace)).run(view);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.view.layout", violation.getType());
        assertEquals("The view with key \"SystemLandscape-001\" has an automatically generated view key, which may cause manual layout information to be lost in the future.", violation.getMessage());
    }

    @Test
    public void run_GeneratedKeyAndAutomaticLayout() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("", "Description");
        view.enableAutomaticLayout();

        Violation violation = new ManualLayoutInspection(new DefaultInspector(workspace)).run(view);
        assertNull(violation);
    }

    @Test
    public void run_NonGeneratedKeyAndManualLayout() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        Violation violation = new ManualLayoutInspection(new DefaultInspector(workspace)).run(view);
        assertNull(violation);
    }

    @Test
    public void run_NonGeneratedKeyAndAutomaticLayout() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.enableAutomaticLayout();

        Violation violation = new ManualLayoutInspection(new DefaultInspector(workspace)).run(view);
        assertNull(violation);
    }

}
