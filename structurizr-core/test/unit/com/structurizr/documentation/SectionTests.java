package com.structurizr.documentation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SectionTests {

    @Test
    void construction() {
        Section section = new Section("Title", Format.Markdown, "Content");

        assertEquals("Title", section.getTitle());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Content", section.getContent());
    }

}