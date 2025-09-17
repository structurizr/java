package com.structurizr.export.plantuml;

import com.structurizr.export.IndentingWriter;
import com.structurizr.view.Border;

import java.util.Base64;

import static java.lang.String.format;

class PlantUMLBoundaryStyle extends PlantUMLStyle {

    private final String background;
    private final String color;
    private final String stroke;
    private final int strokeWidth;
    private final String lineStyle;
    private final int fontSize;
    private final boolean shadow;

    PlantUMLBoundaryStyle(String name, String background, String color, String stroke, int strokeWidth, Border border, int fontSize, boolean shadow) {
        super(name);

        this.background = background;
        this.color = color;
        this.stroke = stroke;
        this.strokeWidth = strokeWidth;

        switch (border) {
            case Dotted:
                this.lineStyle = (strokeWidth) + "-" + (strokeWidth);
                break;
            case Dashed:
                this.lineStyle = (strokeWidth * 5) + "-" + (strokeWidth * 5);
                break;
            default:
                this.lineStyle = "0";
                break;
        }

        this.fontSize = fontSize;
        this.shadow = shadow;
    }

    @Override
    String getClassSelector() {
        return "Boundary-" + Base64.getEncoder().encodeToString(name.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlantUMLBoundaryStyle that = (PlantUMLBoundaryStyle) o;
        return getClassSelector().equals(that.getClassSelector());
    }

    @Override
    public String toString() {
        IndentingWriter writer = new IndentingWriter();
        writer.indent();
        writer.writeLine(format("// %s", name));
        writer.writeLine(format(".%s {", getClassSelector()));
        writer.indent();

        writer.writeLine(String.format("BackgroundColor: %s;", background));
        writer.writeLine(String.format("LineColor: %s;", stroke));
        writer.writeLine(String.format("LineStyle: %s;", lineStyle));
        writer.writeLine(String.format("LineThickness: %s;", strokeWidth));
        writer.writeLine(String.format("FontColor: %s;", color));
        writer.writeLine(String.format("FontSize: %s;", fontSize));
        writer.writeLine("HorizontalAlignment: center;");
        writer.writeLine(String.format("Shadowing: %s;", shadow ? SHADOW_DISTANCE : 0));

        writer.outdent();
        writer.writeLine("}");
        writer.outdent();
        writer.writeLine();

        return writer.toString();
    }

}