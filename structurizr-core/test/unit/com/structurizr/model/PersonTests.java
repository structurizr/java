package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonTests extends AbstractWorkspaceTestBase {

    private Person person = model.addPerson(Location.External, "Person", "Description");

    @Test
    public void test_getCanonicalName() {
        assertEquals("/Person", person.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsASlashCharacter() {
        person.setName("Name1/Name2");
        assertEquals("/Name1Name2", person.getCanonicalName());
    }

}