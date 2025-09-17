package com.structurizr.export.plantuml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLLegendStyleTests {

    @Test
    void test() {
        PlantUMLLegendStyle style = new PlantUMLLegendStyle();

        assertEquals("""
                  // transparent element for relationships in legend
                  .Element-Transparent {
                    BackgroundColor: transparent;
                    LineColor: transparent;
                    FontColor: transparent;
                  }
                """, style.toString());
    }

}
