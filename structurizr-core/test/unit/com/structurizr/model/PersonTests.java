package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTests extends AbstractWorkspaceTestBase {

    @Test
    void getCanonicalName() {
        Person person = model.addPerson("Person", "Description");
        assertEquals("Person://Person", person.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        Person person = model.addPerson("Person", "Description");
        person.setName("Name1/.Name2");
        assertEquals("Person://Name1Name2", person.getCanonicalName());
    }

    @Test
    void getParent_ReturnsNull() {
        Person person = model.addPerson("Person", "Description");
        assertNull(person.getParent());
    }

    @Test
    void removeTags_DoesNotRemoveRequiredTags() {
        Person person = model.addPerson("Person", "Description");
        assertTrue(person.getTags().contains(Tags.ELEMENT));
        assertTrue(person.getTags().contains(Tags.PERSON));

        person.removeTag(Tags.PERSON);
        person.removeTag(Tags.ELEMENT);

        assertTrue(person.getTags().contains(Tags.ELEMENT));
        assertTrue(person.getTags().contains(Tags.PERSON));
    }

    @Test
    void interactsWith_AddsARelationshipWhenTheDescriptionIsSpecified() {
        Person person1 = model.addPerson("Person 1", "Description");
        Person person2 = model.addPerson("Person 2", "Description");

        person1.interactsWith(person2, "Sends an e-mail to");
        assertEquals(1, person1.getRelationships().size());

        Relationship relationship = person1.getRelationships().iterator().next();
        assertSame(person1, relationship.getSource());
        assertSame(person2, relationship.getDestination());
        assertEquals("Sends an e-mail to", relationship.getDescription());
        assertNull(relationship.getTechnology());
        assertNull(relationship.getInteractionStyle());
    }

    @Test
    void interactsWith_AddsARelationshipWhenTheDescriptionAndTechnologyAreSpecified() {
        Person person1 = model.addPerson("Person 1", "Description");
        Person person2 = model.addPerson("Person 2", "Description");

        person1.interactsWith(person2, "Sends a message to", "E-mail");
        assertEquals(1, person1.getRelationships().size());

        Relationship relationship = person1.getRelationships().iterator().next();
        assertSame(person1, relationship.getSource());
        assertSame(person2, relationship.getDestination());
        assertEquals("Sends a message to", relationship.getDescription());
        assertEquals("E-mail", relationship.getTechnology());
        assertNull(relationship.getInteractionStyle());
    }

    @Test
    void interactsWith_AddsARelationshipWhenTheDescriptionAndTechnologyAndInteractionStyleAreSpecified() {
        Person person1 = model.addPerson("Person 1", "Description");
        Person person2 = model.addPerson("Person 2", "Description");

        person1.interactsWith(person2, "Sends a message to", "E-mail", InteractionStyle.Asynchronous);
        assertEquals(1, person1.getRelationships().size());

        Relationship relationship = person1.getRelationships().iterator().next();
        assertSame(person1, relationship.getSource());
        assertSame(person2, relationship.getDestination());
        assertEquals("Sends a message to", relationship.getDescription());
        assertEquals("E-mail", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());
    }

    @Test
    void setLocation_SetsTheLocationToUnspecified_WhenNullIsPassed() {
        Person person = model.addPerson("Person", "Description");
        person.setLocation(null);
        assertEquals(Location.Unspecified, person.getLocation());
    }

}