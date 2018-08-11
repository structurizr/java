package com.structurizr.documentation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SectionTests {

    @Test
    public void test_setType_ProvidesBackwardsCompatibility() {
        Section section = new Section();
        section.setType("Title"); // older clients use the type property
        assertEquals("Title", section.getTitle());
    }

}