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

public class GeneratedKeyInspectionTests {

    @Test
    public void run_GeneratedKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("", "Description");

        Violation violation = new GeneratedKeyInspection(new DefaultInspector(workspace)).run(view);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.view.key", violation.getType());
        assertEquals("The view with key \"SystemLandscape-001\" has an automatically generated view key, which is not guaranteed to be stable over time.", violation.getMessage());
    }

    @Test
    public void run_NonGeneratedKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        Violation violation = new GeneratedKeyInspection(new DefaultInspector(workspace)).run(view);
        assertNull(violation);
    }

}
