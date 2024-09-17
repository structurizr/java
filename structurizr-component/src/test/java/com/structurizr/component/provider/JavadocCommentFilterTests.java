package com.structurizr.component.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JavadocCommentFilterTests {

    @Test
    public void test_filter_ReturnsNull_WhenGivenNull() {
        assertNull(new JavadocCommentFilter().filter(null));
    }

    @Test
    public void test_filter_ReturnsTheOriginalText_WhenNoMaxLengthHasBeenSpecified()
    {
        assertEquals("Here is some text.", new JavadocCommentFilter().filter("Here is some text."));
    }

    @Test
    public void test_filter_FiltersJavadocLinkTags()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter().filter("Uses {@link SomeClass} and {@link AnotherClass} to do some work."));
    }

    @Test
    public void test_filter_FiltersJavadocLinkTagsWithLabels()
    {
        assertEquals("Uses some class and another class to do some work.", new JavadocCommentFilter().filter("Uses {@link SomeClass some class} and {@link AnotherClass another class} to do some work."));
    }

    @Test
    public void test_filter_FiltersHtml()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter().filter("Uses <b>SomeClass</b> and <b>AnotherClass</b> to do some work."));
    }

    @Test
    public void test_filter_FiltersLineBreaks()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter().filter("Uses SomeClass and AnotherClass\nto do some work."));
    }

}