package com.structurizr.export.plantuml;

import com.structurizr.view.Border;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLBoundaryStyleTests {

    @Test
    void test() {
        PlantUMLBoundaryStyle style = new PlantUMLBoundaryStyle("Name", "#ffffff", "#444444", "#ff0000", 4, Border.Dotted, 24, false);

        assertEquals("""
                  // Name
                  .Boundary-TmFtZQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #ff0000;
                    LineStyle: 4-4;
                    LineThickness: 4;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                """, style.toString());
    }

}
