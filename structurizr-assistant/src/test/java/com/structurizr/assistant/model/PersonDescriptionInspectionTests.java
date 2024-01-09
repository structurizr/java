package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersonDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        Person person = workspace.getModel().addPerson("Name");

        Recommendation recommendation = new PersonDescriptionInspection(workspace).run(person);
        Assertions.assertEquals(Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.person.description", recommendation.getType());
        assertEquals("Add a description to the person named \"Name\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        Person person = workspace.getModel().addPerson("Name", "Description");

        Recommendation recommendation = new PersonDescriptionInspection(workspace).run(person);
        assertNull(recommendation);
    }

}
