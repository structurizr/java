package com.structurizr.export.plantuml;

import com.structurizr.export.IndentingWriter;
import com.structurizr.view.LineStyle;

import java.util.Base64;

import static java.lang.String.format;

class PlantUMLRelationshipStyle extends PlantUMLStyle {

    private final String color;
    private final String lineStyle;
    private final int thickness;
    private final int fontSize;

    PlantUMLRelationshipStyle(String name, String color, LineStyle lineStyle, int thickness, int fontSize) {
        super(name);

        this.color = color;
        this.thickness = thickness;
        this.fontSize = fontSize;

        switch (lineStyle) {
            case Dotted:
                this.lineStyle = (thickness) + "-" + (thickness);
                break;
            case Dashed:
                this.lineStyle = (thickness * 5) + "-" + (thickness * 5);
                break;
            default:
                this.lineStyle = "0";
                break;
        }
    }

    @Override
    String getClassSelector() {
        return "Relationship-" + Base64.getEncoder().encodeToString(name.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlantUMLRelationshipStyle that = (PlantUMLRelationshipStyle) o;
        return getClassSelector().equals(that.getClassSelector());
    }

    @Override
    public String toString() {
        IndentingWriter writer = new IndentingWriter();
        writer.indent();
        writer.writeLine(format("// %s", name));
        writer.writeLine(format(".%s {", getClassSelector()));
        writer.indent();

        writer.writeLine(String.format("LineThickness: %s;", thickness));
        writer.writeLine(String.format("LineStyle: %s;", lineStyle));
        writer.writeLine(String.format("LineColor: %s;", color));
        writer.writeLine(String.format("FontColor: %s;", color));
        writer.writeLine(String.format("FontSize: %s;", fontSize));

        writer.outdent();
        writer.writeLine("}");
        writer.outdent();
        writer.writeLine();

        return writer.toString();
    }

}