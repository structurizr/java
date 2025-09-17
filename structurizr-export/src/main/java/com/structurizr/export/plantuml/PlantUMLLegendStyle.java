package com.structurizr.export.plantuml;

import com.structurizr.export.IndentingWriter;

import static java.lang.String.format;

class PlantUMLLegendStyle extends PlantUMLStyle {

    PlantUMLLegendStyle() {
        super("Element-Transparent");
    }

    @Override
    String getClassSelector() {
        return "Element-Transparent";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlantUMLLegendStyle that = (PlantUMLLegendStyle) o;
        return getClassSelector().equals(that.getClassSelector());
    }

    @Override
    public String toString() {
        IndentingWriter writer = new IndentingWriter();
        writer.indent();
        writer.writeLine("// transparent element for relationships in legend");
        writer.writeLine(format(".%s {", getClassSelector()));
        writer.indent();

        writer.writeLine("BackgroundColor: transparent;");
        writer.writeLine("LineColor: transparent;");
        writer.writeLine("FontColor: transparent;");

        writer.outdent();
        writer.writeLine("}");
        writer.outdent();
        writer.writeLine();

        return writer.toString();
    }
}