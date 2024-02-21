package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
PersonDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        Person person = workspace.getModel().addPerson("Name");

        Violation violation = new PersonDescriptionInspection(new DefaultInspector(workspace)).run(person);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.person.description", violation.getType());
        assertEquals("The person \"Name\" is missing a description.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        Person person = workspace.getModel().addPerson("Name", "Description");

        Violation violation = new PersonDescriptionInspection(new DefaultInspector(workspace)).run(person);
        assertNull(violation);
    }

}
