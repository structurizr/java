package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoftwareSystemParserTests extends AbstractTests {

    private final SoftwareSystemParser parser = new SoftwareSystemParser();
    private Archetype archetype = new Archetype("name", "type");

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("softwareSystem", "name", "description", "tags", "extra"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: softwareSystem <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("softwareSystem"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: softwareSystem <name> [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASoftwareSystem() {
        parser.parse(context(), tokens("softwareSystem", "Name"), archetype);

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("", softwareSystem.getDescription());
        assertEquals("Element,Software System", softwareSystem.getTags());
    }

    @Test
    void test_parse_CreatesASoftwareSystemWithADescription() {
        parser.parse(context(), tokens("softwareSystem", "Name", "Description"), archetype);

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("Description", softwareSystem.getDescription());
        assertEquals("Element,Software System", softwareSystem.getTags());
    }

    @Test
    void test_parse_CreatesASoftwareSystemWithADescriptionAndTags() {
        parser.parse(context(), tokens("softwareSystem", "Name", "Description", "Tag 1, Tag 2"), archetype);

        assertEquals(1, model.getElements().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("Description", softwareSystem.getDescription());
        assertEquals("Element,Software System,Tag 1,Tag 2", softwareSystem.getTags());
    }

}