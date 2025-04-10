package com.structurizr.dsl;

import com.structurizr.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonParserTests extends AbstractTests {

    private final PersonParser parser = new PersonParser();
    private Archetype archetype = new Archetype("name", "type");

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("person", "name", "description", "tags", "tokens"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: person <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("person"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: person <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAPerson() {
        parser.parse(context(), tokens("person", "User"), archetype);

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("", user.getDescription());
        assertEquals("Element,Person", user.getTags());
    }

    @Test
    void test_parse_CreatesAPersonWithADescription() {
        parser.parse(context(), tokens("person", "User", "Description"), archetype);

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("Description", user.getDescription());
        assertEquals("Element,Person", user.getTags());
    }

    @Test
    void test_parse_CreatesAPersonWithADescriptionAndTags() {
        parser.parse(context(), tokens("person", "User", "Description", "Tag 1, Tag 2"), archetype);

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("Description", user.getDescription());
        assertEquals("Element,Person,Tag 1,Tag 2", user.getTags());
    }

    @Test
    void test_parse_CreatesAPersonWithADescriptionAndTagsBasedUponAnArchetype() {
        archetype = new Archetype("name", "type");
        archetype.setDescription("Default Description");
        archetype.addTags("Default Tag");

        parser.parse(context(), tokens("person", "User", "Description", "Tag 1, Tag 2"), archetype);

        assertEquals(1, model.getElements().size());
        Person user = model.getPersonWithName("User");
        assertNotNull(user);
        assertEquals("Description", user.getDescription()); // overridden from archetype
        assertEquals("Element,Person,Default Tag,Tag 1,Tag 2", user.getTags());
    }

}