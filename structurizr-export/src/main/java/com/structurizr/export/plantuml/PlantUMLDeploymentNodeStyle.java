package com.structurizr.export.plantuml;

import com.structurizr.export.IndentingWriter;
import com.structurizr.view.Border;

import java.util.Base64;

import static java.lang.String.format;

class PlantUMLDeploymentNodeStyle extends PlantUMLStyle {

    private final String background;
    private final String color;
    private final String stroke;
    private final int strokeWidth;
    private final String lineStyle;
    private final int fontSize;
    private final String icon;
    private final boolean shadow;
    private Integer width; // only used for the legend

    PlantUMLDeploymentNodeStyle(String name, String background, String color, String stroke, int strokeWidth, Border border, int fontSize, String icon, boolean shadow) {
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
        this.icon = icon;
        this.shadow = shadow;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getIcon() {
        return icon;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    String getClassSelector() {
        return "DeploymentNode-" + Base64.getEncoder().encodeToString(name.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlantUMLDeploymentNodeStyle that = (PlantUMLDeploymentNodeStyle) o;
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
        if (width != null) {
            writer.writeLine(String.format("MaximumWidth: %s;", width));
        }

        writer.outdent();
        writer.writeLine("}");
        writer.outdent();
        writer.writeLine();

        return writer.toString();
    }

}