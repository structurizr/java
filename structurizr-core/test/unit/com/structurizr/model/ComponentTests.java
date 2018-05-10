package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class ComponentTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    public void test_getName_ReturnsTheGivenName_WhenANameIsGiven() {
        Component component = new Component();
        component.setName("Some name");
        assertEquals("Some name", component.getName());
    }

    @Test
    public void test_getCanonicalName() {
        Component component = container.addComponent("Component", "Description");
        assertEquals("/System/Container/Component", component.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsASlashCharacter() {
        Component component = container.addComponent("Name1/Name2", "Description");
        assertEquals("/System/Container/Name1Name2", component.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentContainer() {
        Component component = container.addComponent("Component", "Description");
        assertEquals(container, component.getParent());
    }

    @Test
    public void test_getContainer_ReturnsTheParentContainer() {
        Component component = container.addComponent("Name", "Description");
        assertEquals(container, component.getContainer());
    }

    @Test
    public void test_removeTags_DoesNotRemoveRequiredTags() {
        Component component = new Component();
        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));

        component.removeTag(Tags.COMPONENT);
        component.removeTag(Tags.ELEMENT);

        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));
    }

    @Test
    public void test_technologyProperty() {
        Component component = new Component();
        assertNull(component.getTechnology());

        component.setTechnology("Spring Bean");
        assertEquals("Spring Bean", component.getTechnology());
    }

    @Test
    public void test_setType_ThrowsAnExceptionWhenPassedNull() {
        Component component = new Component();
        try {
            component.setType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A fully qualified name must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_setType_AddsAPrimaryCodeElement_WhenPassedAFullyQualifiedTypeName() {
        Component component = new Component();
        component.setType("com.structurizr.web.HomePageController");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("HomePageController", codeElement.getName());
        assertEquals("com.structurizr.web.HomePageController", codeElement.getType());
        assertEquals(CodeElementRole.Primary, codeElement.getRole());
    }

    @Test
    public void test_setType_OverwritesThePrimaryCodeElement_WhenCalledMoreThanOnce() {
        Component component = new Component();
        component.setType("com.structurizr.web.HomePageController");
        component.setType("com.structurizr.web.SomeOtherController");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("SomeOtherController", codeElement.getName());
        assertEquals("com.structurizr.web.SomeOtherController", codeElement.getType());
        assertEquals(CodeElementRole.Primary, codeElement.getRole());

    }

    @Test
    public void test_addSupportingType_ThrowsAnExceptionWhenPassedNull() {
        Component component = new Component();
        try {
            component.addSupportingType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A fully qualified name must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_addSupportingType_AddsASupportingCodeElement_WhenPassedAFullyQualifiedTypeName() {
        Component component = new Component();
        component.addSupportingType("com.structurizr.web.HomePageViewModel");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("HomePageViewModel", codeElement.getName());
        assertEquals("com.structurizr.web.HomePageViewModel", codeElement.getType());
        assertEquals(CodeElementRole.Supporting, codeElement.getRole());
    }

    @Test
    public void test_addSupportingType_ReturnsSameCodeElement_WhenPassedSameFullyQualifiedType() {
        Component component = new Component();
        CodeElement code1 = component.addSupportingType("com.structurizr.web.HomePageViewModel");
        CodeElement code2 = component.addSupportingType("com.structurizr.web.HomePageViewModel");

        assertTrue(code1 == code2);
        assertEquals(1, component.getCode().size());
    }

}
