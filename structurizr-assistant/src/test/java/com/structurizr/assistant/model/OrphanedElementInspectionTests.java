package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrphanedElementInspectionTests {

    @Test
    public void run_WithOrphan() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");

        Recommendation recommendation = new OrphanedElementInspection(workspace).run(a);
        Assertions.assertEquals(Recommendation.Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.element.orphaned", recommendation.getType());
        assertEquals("The software system named \"A\" is orphaned - add a relationship to/from it, or consider removing it from the model.", recommendation.getDescription());
    }

    @Test
    public void run_WithoutOrphan() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        a.uses(b, "Uses");

        Recommendation recommendation = new OrphanedElementInspection(workspace).run(a);
        assertNull(recommendation);

        recommendation = new OrphanedElementInspection(workspace).run(b);
        assertNull(recommendation);
    }

}
