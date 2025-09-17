package com.structurizr.export.plantuml;

import com.structurizr.view.Border;
import com.structurizr.view.Shape;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLElementStyleTests {

    @Test
    void test() {
        PlantUMLElementStyle style = new PlantUMLElementStyle("Name", Shape.RoundedBox, 450, "#ffffff", "#444444", "#ff0000", 4, Border.Dotted, 24, "icon", false);

        assertEquals("""
                  // Name
                  .Element-TmFtZQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #ff0000;
                    LineStyle: 4-4;
                    LineThickness: 4;
                    RoundCorner: 20;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                """, style.toString());
    }

}
