package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomElementParserTests extends AbstractTests {

    private CustomElementParser parser = new CustomElementParser();
    private Archetype archetype = new Archetype("name", "type");

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("element", "name", "metadata", "description", "tags", "extra"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: element <name> [metadata] [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("element"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: element <name> [metadata] [description] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesACustomElement() {
        parser.parse(context(), tokens("element", "Name"), archetype);

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadata() {
        parser.parse(context(), tokens("element", "Name", "Box"), archetype);

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadataAndDescription() {
        parser.parse(context(), tokens("element", "Name", "Box", "Description"), archetype);

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("Description", element.getDescription());
        assertEquals("Element", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadataAndDescriptionAndTags() {
        parser.parse(context(), tokens("element", "Name", "Box", "Description", "Tag 1, Tag 2"), archetype);

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("Description", element.getDescription());
        assertEquals("Element,Tag 1,Tag 2", element.getTags());
    }

    @Test
    void test_parse_CreatesACustomElementWithMetadataAndDescriptionAndTagsBasedUponAnArchetype() {
        archetype = new Archetype("name", "type");
        archetype.setDescription("Default Description");
        archetype.addTags("Default Tag");

        parser.parse(context(), tokens("element", "Name", "Box", "Description", "Tag 1, Tag 2"), archetype);

        assertEquals(1, model.getElements().size());
        CustomElement element = model.getCustomElementWithName("Name");
        assertNotNull(element);
        assertEquals("Box", element.getMetadata());
        assertEquals("Description", element.getDescription()); // overridden from archetype
        assertEquals("Element,Default Tag,Tag 1,Tag 2", element.getTags());
    }

}