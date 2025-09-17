package com.structurizr.export.plantuml;

import com.structurizr.view.LineStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLRelationshipStyleTests {

    @Test
    void solid() {
        PlantUMLRelationshipStyle style = new PlantUMLRelationshipStyle("Relationship", "#ff0000", LineStyle.Solid, 3, 24);

        assertEquals("""
                // Relationship
                .Relationship-UmVsYXRpb25zaGlw {
                  LineThickness: 3;
                  LineStyle: 0;
                  LineColor: #ff0000;
                  FontColor: #ff0000;
                  FontSize: 24;
                }
              """, style.toString());
    }

    @Test
    void dashed() {
        PlantUMLRelationshipStyle style = new PlantUMLRelationshipStyle("Relationship", "#ff0000", LineStyle.Dashed, 3, 24);

        assertEquals("""
                // Relationship
                .Relationship-UmVsYXRpb25zaGlw {
                  LineThickness: 3;
                  LineStyle: 15-15;
                  LineColor: #ff0000;
                  FontColor: #ff0000;
                  FontSize: 24;
                }
              """, style.toString());
    }

    @Test
    void dotted() {
        PlantUMLRelationshipStyle style = new PlantUMLRelationshipStyle("Relationship", "#ff0000", LineStyle.Dotted, 3, 24);

        assertEquals("""
                // Relationship
                .Relationship-UmVsYXRpb25zaGlw {
                  LineThickness: 3;
                  LineStyle: 3-3;
                  LineColor: #ff0000;
                  FontColor: #ff0000;
                  FontSize: 24;
                }
              """, style.toString());
    }

}
