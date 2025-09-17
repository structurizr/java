package com.structurizr.export.plantuml;

import com.structurizr.view.LineStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLRootStyleTests {

    @Test
    void noFont() {
        PlantUMLRootStyle style = new PlantUMLRootStyle(
                "#ffffff",
                "#444444",
                null);

        assertEquals("""
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                """, style.toString());

        style = new PlantUMLRootStyle(
                "#ffffff",
                "#444444",
                "");

        assertEquals("""
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                """, style.toString());
    }

    @Test
    void font() {
        PlantUMLRootStyle style = new PlantUMLRootStyle(
                "#ffffff",
                "#444444",
                "Courier");

        assertEquals("""
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                    FontName: Courier;
                  }
                """, style.toString());
    }

}
