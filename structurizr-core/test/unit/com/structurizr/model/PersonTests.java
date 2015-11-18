package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void test_getParent_ReturnsNull() {
        assertNull(person.getParent());
    }

    @Test
    public void test_removeTags_DoesNotRemoveRequiredTags() {
        assertTrue(person.getTags().contains(Tags.ELEMENT));
        assertTrue(person.getTags().contains(Tags.PERSON));

        person.removeTag(Tags.PERSON);
        person.removeTag(Tags.ELEMENT);

        assertTrue(person.getTags().contains(Tags.ELEMENT));
        assertTrue(person.getTags().contains(Tags.PERSON));
    }

}