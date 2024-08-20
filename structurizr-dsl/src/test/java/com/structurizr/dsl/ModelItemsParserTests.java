package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ModelItemsParserTests extends AbstractTests {

    private final ModelItemsParser parser = new ModelItemsParser();

    @Test
    void test_parseTags_ThrowsAnException_WhenNoTagsAreSpecified() {
        try {
            parser.parseTags(null, tokens("tags"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: tags <tags> [tags]", e.getMessage());
        }
    }

    @Test
    void test_parseTags_AddsTheTags_WhenTagsAreSpecified() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");

        ElementsDslContext context = new ElementsDslContext(null, Set.of(a, b, c));
        
        parser.parseTags(context, tokens("tags", "Tag 1"));
        for (Element element : context.getElements()) {
            assertEquals(3, element.getTagsAsSet().size());
            assertTrue(element.getTagsAsSet().contains("Tag 1"));
        }

        parser.parseTags(context, tokens("tags", "Tag 1, Tag 2, Tag 3"));
        for (Element element : context.getElements()) {
            assertEquals(5, element.getTagsAsSet().size());
            assertTrue(element.getTagsAsSet().contains("Tag 1"));
            assertTrue(element.getTagsAsSet().contains("Tag 2"));
            assertTrue(element.getTagsAsSet().contains("Tag 3"));
        }
    }

}