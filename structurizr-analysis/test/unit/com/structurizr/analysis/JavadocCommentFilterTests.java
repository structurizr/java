package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JavadocCommentFilterTests {

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenZeroIsSpecified() {
        new JavadocCommentFilter(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnIllegalArgumentException_WhenANegativeNumberIsSpecified() {
        new JavadocCommentFilter(-1);
    }

    @Test
    public void test_filterAndTruncate_ReturnsNull_WhenGivenNull() {
        assertNull(new JavadocCommentFilter(null).filterAndTruncate(null));
    }

    @Test
    public void test_filterAndTruncate_ReturnsTheOriginalText_WhenNoMaxLengthHasBeenSpecified()
    {
        assertEquals("Here is some text.", new JavadocCommentFilter(null).filterAndTruncate("Here is some text."));
    }

    @Test
    public void test_filterAndTruncate_TruncatesTheTextWhenAMaxLengthHasBeenSpecified()
    {
        assertEquals("Here...", new JavadocCommentFilter(7).filterAndTruncate("Here is some text."));
    }

    @Test
    public void test_filterAndTruncate_FiltersJavadocLinkTags()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter(null).filterAndTruncate("Uses {@link SomeClass} and {@link AnotherClass} to do some work."));
    }

    @Test
    public void test_filterAndTruncate_FiltersJavadocLinkTagsWithLabels()
    {
        assertEquals("Uses some class and another class to do some work.", new JavadocCommentFilter(null).filterAndTruncate("Uses {@link SomeClass some class} and {@link AnotherClass another class} to do some work."));
    }

    @Test
    public void test_filterAndTruncate_FiltersHtml()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter(null).filterAndTruncate("Uses <b>SomeClass</b> and <b>AnotherClass</b> to do some work."));
    }

    @Test
    public void test_filterAndTruncate_FiltersLineBreaks()
    {
        assertEquals("Uses SomeClass and AnotherClass to do some work.", new JavadocCommentFilter(null).filterAndTruncate("Uses SomeClass and AnotherClass\nto do some work."));
    }

}
