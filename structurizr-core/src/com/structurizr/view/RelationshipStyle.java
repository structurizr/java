package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RelationshipStyle {

    private String tag;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer thickness;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String color;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer fontSize;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer width;

    public RelationshipStyle() {
    }

    public RelationshipStyle(String tag) {
        this.tag = tag;
    }

    public RelationshipStyle(String tag, Integer thickness, String color, Integer fontSize, Integer width) {
        this.tag = tag;
        this.thickness = thickness;
        this.color = color;
        this.fontSize = fontSize;
        this.width = width;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
