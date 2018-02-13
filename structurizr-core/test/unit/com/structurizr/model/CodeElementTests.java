package com.structurizr.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CodeElementTests {

    @Test
    public void test_construction_WhenAFullyQualifiedNameIsSpecified() {
        CodeElement codeElement = newCodeElement();
        assertEquals("SomeComponent", codeElement.getName());
        assertEquals("com.structurizr.component.SomeComponent", codeElement.getType());
    }

    @Test
    public void test_construction_WhenAFullyQualifiedNameIsSpecifiedInTheDefaultPackage() {
        CodeElement codeElement = new CodeElement("SomeComponent", "SomeComponent", null);
        assertEquals("SomeComponent", codeElement.getName());
        assertEquals("SomeComponent", codeElement.getType());
    }

    @Test
    public void test_languageProperty() {
        CodeElement codeElement = newCodeElement();
        assertEquals("Java", codeElement.getLanguage());

        codeElement.setLanguage("Scala");
        assertEquals("Scala", codeElement.getLanguage());
    }

    @Test
    public void test_categoryProperty() {
        CodeElement codeElement = newCodeElement();
        assertNull(codeElement.getCategory());

        codeElement.setCategory("class");
        assertEquals("class", codeElement.getCategory());
    }

    @Test
    public void test_visibilityProperty() {
        CodeElement codeElement = newCodeElement();
        assertNull(codeElement.getVisibility());

        codeElement.setVisibility("package");
        assertEquals("package", codeElement.getVisibility());
    }

    @Test
    public void test_setUrl() {
        CodeElement codeElement = newCodeElement();
        codeElement.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", codeElement.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        CodeElement codeElement = newCodeElement();
        codeElement.setUrl("htt://blah");
    }

    @Test
    public void test_setUrl_DoesNothing_WhenANullUrlIsSpecified() {
        CodeElement codeElement = newCodeElement();
        codeElement.setUrl(null);
        assertNull(codeElement.getUrl());
    }

    @Test
    public void test_setUrl_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        CodeElement codeElement = newCodeElement();
        codeElement.setUrl(" ");
        assertNull(codeElement.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenANullFullyQualifiedNameIsSpecified() {
        new CodeElement(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenAnEmptyFullyQualifiedNameIsSpecified() {
        new CodeElement("\t", " ", "        ");
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToNull() {
        CodeElement codeElement = newCodeElement();
        assertFalse(codeElement.equals(null));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToDifferentTypeOfObject() {
        CodeElement codeElement = newCodeElement();
        assertFalse(codeElement.equals("hello"));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToAnotherCodeElementWithADifferentType() {
        CodeElement codeElement1 = newCodeElement1();
        CodeElement codeElement2 = newCodeElement2();
        assertFalse(codeElement1.equals(codeElement2));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenComparedToAnotherCodeElementWithTheSameType() {
        CodeElement codeElement1 = newCodeElement1();
        CodeElement codeElement2 = newCodeElement1();
        assertTrue(codeElement1.equals(codeElement2));
    }

    private CodeElement newCodeElement() {
        return new CodeElement(
                "SomeComponent",
                "com.structurizr.component.SomeComponent",
                "com.structurizr.component");
    }

    private CodeElement newCodeElement1() {
        return new CodeElement(
                "SomeComponent1",
                "com.structurizr.component.SomeComponent1",
                "com.structurizr.component");
    }

    private CodeElement newCodeElement2() {
        return new CodeElement(
                "SomeComponent2",
                "com.structurizr.component.SomeComponent2",
                "com.structurizr.component");
    }
}
