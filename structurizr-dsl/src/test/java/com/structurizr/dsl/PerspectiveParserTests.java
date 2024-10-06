package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Perspective;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PerspectiveParserTests extends AbstractTests {

    private final PerspectiveParser parser = new PerspectiveParser();

    @Test
    void test_parsePerspective_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            PerspectivesDslContext context = new PerspectivesDslContext((ModelItem)null);
            parser.parse(context, tokens("name", "description", "value", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <name> <description> [value]", e.getMessage());
        }
    }

    @Test
    void test_parsePerspective_ThrowsAnException_WhenNoNameIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            PerspectivesDslContext context = new PerspectivesDslContext(softwareSystem);
            parser.parse(context, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <name> <description> [value]", e.getMessage());
        }
    }

    @Test
    void test_parsePerspective_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            PerspectivesDslContext context = new PerspectivesDslContext(softwareSystem);
            parser.parse(context, tokens("name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <name> <description> [value]", e.getMessage());
        }
    }

    @Test
    void test_parsePerspective_AddsThePerspective_WhenADescriptionIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        PerspectivesDslContext context = new PerspectivesDslContext(softwareSystem);
        parser.parse(context, tokens("Security", "Description"));

        Perspective perspective = softwareSystem.getPerspectives().stream().filter(p -> p.getName().equals("Security")).findFirst().get();
        assertEquals("Description", perspective.getDescription());
        assertEquals("", perspective.getValue());
    }

    @Test
    void test_parsePerspective_AddsThePerspective_WhenADescriptionAndValueIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        PerspectivesDslContext context = new PerspectivesDslContext(softwareSystem);
        parser.parse(context, tokens("Security", "Description", "Value"));

        Perspective perspective = softwareSystem.getPerspectives().stream().filter(p -> p.getName().equals("Security")).findFirst().get();
        assertEquals("Description", perspective.getDescription());
        assertEquals("Value", perspective.getValue());
    }

}