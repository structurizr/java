package com.structurizr.export.plantuml;

import com.structurizr.view.Border;
import com.structurizr.view.LineStyle;
import com.structurizr.view.RelationshipStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLGroupStyleTests {

    @Test
    void test() {
        PlantUMLGroupStyle style = new PlantUMLGroupStyle("Name", "#ffffff", "#444444", "#ff0000", 4, Border.Dotted, 24, false);

        assertEquals("""
                  // Name
                  .Group-TmFtZQ== {
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
