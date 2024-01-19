package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ElementStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class

ElementStyleMetadataInspectionTests {

    @Test
    public void run_WithMetadataFalse() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag").metadata(false);

        Violation violation = new ElementStyleMetadataInspection(new DefaultInspector(workspace)).run(elementStyle);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.styles.element.metadata", violation.getType());
        assertEquals("The element style for tag \"Tag\" has metadata hidden, which may introduce ambiguity on rendered diagrams.", violation.getMessage());
    }

    @Test
    public void run_WithMetadataTrue() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag").metadata(true);

        Violation violation = new ElementStyleMetadataInspection(new DefaultInspector(workspace)).run(elementStyle);
        assertNull(violation);
    }

    @Test
    public void run_WithMetadataUnset() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag");

        Violation violation = new ElementStyleMetadataInspection(new DefaultInspector(workspace)).run(elementStyle);
        assertNull(violation);
    }

}
