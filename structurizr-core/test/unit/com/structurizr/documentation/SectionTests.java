package com.structurizr.documentation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SectionTests {

    @Test
    public void test_construction() {
        Section section = new Section("Title", Format.Markdown, "Content");

        assertEquals("Title", section.getTitle());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Content", section.getContent());
    }

}