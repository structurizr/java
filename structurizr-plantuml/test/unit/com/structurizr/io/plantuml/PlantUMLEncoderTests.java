package com.structurizr.io.plantuml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlantUMLEncoderTests {

    @Test
    public void test() throws Exception {
        assertEquals("SyfFqhLppCbCJbMmKiX8pSd91m00", new PlantUMLEncoder().encode("Bob->Alice : hello"));
    }

}