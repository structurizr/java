package com.structurizr.importer.diagrams.plantuml;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLEncoderTests {

    @Test
    public void encode() throws Exception {
        File file = new File("./src/test/resources/diagrams/plantuml/with-title.puml");
        String plantuml = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        String encodedPlantuml = new PlantUMLEncoder().encode(plantuml);
        assertEquals("SoWkIImgAStDuIh9BCb9LGXEBInDpKjELKZ9J4mlIinLIAr8p2t8IULooazIqBLJSCp914fQAMIavkJaSpcavgK0zG80", encodedPlantuml);
    }

}