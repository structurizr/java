package com.structurizr.documentation;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FormatFinderTests {

    @Test
    public void test_findFormat_ThrowsAnException_WhenAFileIsNotSpecified() {
        try {
            FormatFinder.findFormat(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_findFormat_ReturnsMarkdown_WhenAMarkdownFileIsSpecified() {
        assertEquals(Format.Markdown, FormatFinder.findFormat(new File("foo.md")));
        assertEquals(Format.Markdown, FormatFinder.findFormat(new File("foo.markdown")));
        assertEquals(Format.Markdown, FormatFinder.findFormat(new File("foo.text")));
    }

    @Test
    public void test_findFormat_ReturnsAsciiDoc_WhenAnAsciiDocFileIsSpecified() {
        assertEquals(Format.AsciiDoc, FormatFinder.findFormat(new File("foo.adoc")));
        assertEquals(Format.AsciiDoc, FormatFinder.findFormat(new File("foo.asciidoc")));
        assertEquals(Format.AsciiDoc, FormatFinder.findFormat(new File("foo.asc")));
    }

}
