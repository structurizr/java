package com.structurizr.dsl;

import com.structurizr.model.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupParserTests extends AbstractTests {

    private final GroupParser parser = new GroupParser();

    @Test
    void parseContext_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseContext(null, tokens("group", "name", "{", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parseContext_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parseContext(null, tokens("group"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parseContext_ThrowsAnException_WhenTheBraceIsMissing() {
        try {
            parser.parseContext(null, tokens("group", "Name", "foo"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name> {", e.getMessage());
        }
    }

    @Test
    void parseContext() {
        ElementGroup group = parser.parseContext(context(), tokens("group", "Group 1", "{"));
        assertEquals("Group 1", group.getName());
        assertTrue(group.getElements().isEmpty());
    }

    @Test
    void parseContext_NestedGroup_ThrowsAnExceptionWhenNestedGroupsAreNotConfigured() {
        ModelDslContext context = new ModelDslContext(new ElementGroup("Group 1"));
        context.setWorkspace(workspace);

        try {
            parser.parseContext(context, tokens("group", "Group 2", "{"));
            fail();
        } catch (Exception e) {
            assertEquals("To use nested groups, please define a model property named structurizr.groupSeparator", e.getMessage());
        }
    }

    @Test
    void parseContext_NestedGroup() {
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        ModelDslContext context = new ModelDslContext(new ElementGroup("Group 1"));
        context.setWorkspace(workspace);

        ElementGroup group = parser.parseContext(context, tokens("group", "Group 2", "{"));
        assertEquals("Group 1/Group 2", group.getName());
        assertTrue(group.getElements().isEmpty());
    }

    @Test
    void parseProperty_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseProperty(null, tokens("group", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: group <name>", e.getMessage());
        }
    }

    @Test
    void parseProperty_ThrowsAnException_WhenTheNameIsMissing() {
        try {
            parser.parseProperty(null, tokens("group"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: group <name>", e.getMessage());
        }
    }

    @Test
    void parseProperty() {
        Component component = workspace.getModel().addSoftwareSystem("Name").addContainer("Name").addComponent("Name");
        ComponentDslContext context = new ComponentDslContext(component);
        context.setWorkspace(workspace);

        parser.parseProperty(context, tokens("group", "Group 1"));
        assertEquals("Group 1", component.getGroup());
    }

    @Test
    void parseProperty_NestedGroup_ThrowsAnExceptionWhenNestedGroupsAreNotConfigured() {
        Component component = workspace.getModel().addSoftwareSystem("Name").addContainer("Name").addComponent("Name");
        component.setGroup("Group 1");
        ComponentDslContext context = new ComponentDslContext(component);
        context.setWorkspace(workspace);

        try {
            parser.parseProperty(context, tokens("group", "Group 2"));
            fail();
        } catch (Exception e) {
            assertEquals("To use nested groups, please define a model property named structurizr.groupSeparator", e.getMessage());
        }
    }

    @Test
    void parseProperty_NestedGroup() {
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        Component component = workspace.getModel().addSoftwareSystem("Name").addContainer("Name").addComponent("Name");
        component.setGroup("Group 1");
        ComponentDslContext context = new ComponentDslContext(component);
        context.setWorkspace(workspace);

        parser.parseProperty(context, tokens("group", "Group 2"));
        assertEquals("Group 1/Group 2", component.getGroup());
    }

}