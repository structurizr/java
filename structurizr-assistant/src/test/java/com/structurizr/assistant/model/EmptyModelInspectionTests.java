package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmptyModelInspectionTests {

    @Test
    public void run_WhenThereAreNoElements() {
        Workspace workspace = new Workspace("Name", "Description");

        Recommendation recommendation = new EmptyModelInspection(workspace).run();
        Assertions.assertEquals(Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.empty", recommendation.getType());
        assertEquals("Add some elements to the model.", recommendation.getDescription());
    }

    @Test
    public void run_WhenThereAreElements() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Name");

        Recommendation recommendation = new EmptyModelInspection(workspace).run();
        assertNull(recommendation);
    }

}
