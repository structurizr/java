package com.structurizr.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CodeElementTests {

    @Test
    public void test_construction_WhenAFullyQualifiedNameIsSpecified() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        assertEquals("SomeComponent", codeElement.getName());
        assertEquals("com.structurizr.component.SomeComponent", codeElement.getType());
        assertEquals("Java", codeElement.getLanguage());
        assertEquals(CodeElementRole.Supporting, codeElement.getRole());
    }

    @Test
    public void test_construction_WhenAFullyQualifiedNameIsSpecifiedInTheDefaultPackage() {
        CodeElement codeElement = new CodeElement("SomeComponent");
        assertEquals("SomeComponent", codeElement.getName());
        assertEquals("SomeComponent", codeElement.getType());
        assertEquals("Java", codeElement.getLanguage());
        assertEquals(CodeElementRole.Supporting, codeElement.getRole());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenANullFullyQualifiedNameIsSpecified() {
        new CodeElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenAnEmptyFullyQualifiedNameIsSpecified() {
        new CodeElement("  ");
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToNull() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        assertFalse(codeElement.equals(null));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToDifferentTypeOfObject() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        assertFalse(codeElement.equals("hello"));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToAnotherCodeElementWithADifferentType() {
        CodeElement codeElement1 = new CodeElement("com.structurizr.component.SomeComponent1");
        CodeElement codeElement2 = new CodeElement("com.structurizr.component.SomeComponent2");
        assertFalse(codeElement1.equals(codeElement2));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToAnotherCodeElementWithTheSameType() {
        CodeElement codeElement1 = new CodeElement("com.structurizr.component.SomeComponent1");
        CodeElement codeElement2 = new CodeElement("com.structurizr.component.SomeComponent1");
        assertTrue(codeElement1.equals(codeElement2));
    }

    @Test
    public void test_setUrl() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        codeElement.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", codeElement.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        codeElement.setUrl("htt://blah");
    }

    @Test
    public void test_setUrl_DoesNothing_WhenANullUrlIsSpecified() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        codeElement.setUrl(null);
        assertNull(codeElement.getUrl());
    }

    @Test
    public void test_setUrl_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        CodeElement codeElement = new CodeElement("com.structurizr.component.SomeComponent");
        codeElement.setUrl(" ");
        assertNull(codeElement.getUrl());
    }

}
