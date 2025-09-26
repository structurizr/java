package com.structurizr.export.plantuml;

import com.structurizr.export.IndentingWriter;
import com.structurizr.util.StringUtils;

import static java.lang.String.format;

class PlantUMLRootStyle extends PlantUMLStyle {

    private final String background;
    private final String color;
    private final String fontName;

    PlantUMLRootStyle(String background, String color, String fontName) {
        super(".root");

        this.background = background;
        this.color = color;
        this.fontName = fontName;
    }

    @Override
    String getClassSelector() {
        return "root";
    }

    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }

    @Override
    public String toString() {
        IndentingWriter writer = new IndentingWriter();
        writer.indent();
        writer.writeLine(format("%s {", getClassSelector()));
        writer.indent();

        writer.writeLine(String.format("BackgroundColor: %s;", background));
        writer.writeLine(String.format("FontColor: %s;", color));
        if (!StringUtils.isNullOrEmpty(fontName)) {
            writer.writeLine(String.format("FontName: %s;", fontName));
        }

        writer.outdent();
        writer.writeLine("}");
        writer.outdent();
        writer.writeLine();

        return writer.toString();
    }
}