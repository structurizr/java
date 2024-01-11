package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.ElementStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ElementStyleMetadataInspectionTests {

    @Test
    public void run_WithMetadataFalse() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag").metadata(false);

        Recommendation recommendation = new ElementStyleMetadataInspection(workspace).run(elementStyle);
        Assertions.assertEquals(Recommendation.Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.views.styles.element.metadata", recommendation.getType());
        assertEquals("The element style for tag \"Tag\" has metadata hidden, which may introduce ambiguity on rendered diagrams.", recommendation.getDescription());
    }

    @Test
    public void run_WithMetadataTrue() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag").metadata(true);

        Recommendation recommendation = new ElementStyleMetadataInspection(workspace).run(elementStyle);
        assertNull(recommendation);
    }

    @Test
    public void run_WithMetadataUnset() {
        Workspace workspace = new Workspace("Name", "Description");
        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag");

        Recommendation recommendation = new ElementStyleMetadataInspection(workspace).run(elementStyle);
        assertNull(recommendation);
    }

}
