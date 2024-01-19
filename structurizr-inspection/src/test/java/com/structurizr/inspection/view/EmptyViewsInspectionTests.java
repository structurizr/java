package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class

EmptyViewsInspectionTests {

    @Test
    public void run_WhenThereAreNoViews() {
        Workspace workspace = new Workspace("Name", "Description");

        Violation violation = new EmptyViewsInspection(new DefaultInspector(workspace)).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.empty", violation.getType());
        assertEquals("Add some views to the workspace.", violation.getMessage());
    }

    @Test
    public void run_WhenThereAreViews() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().createSystemLandscapeView("key", "Description");

        Violation violation = new EmptyViewsInspection(new DefaultInspector(workspace)).run();
        assertNull(violation);
    }

}
