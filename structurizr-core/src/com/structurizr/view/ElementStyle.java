package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ElementStyle {

    public static final int DEFAULT_WIDTH = 450;
    public static final int DEFAULT_HEIGHT = 300;

    private String tag;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer width;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer height;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String background;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String color;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer fontSize;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Shape shape;

    public ElementStyle() {
    }

    public ElementStyle(String tag) {
        this.tag = tag;
    }

    public ElementStyle(String tag, Integer width, Integer height, String background, String color, Integer fontSize) {
        this(tag, width, height, background, color, fontSize, null);
    }

    public ElementStyle(String tag, Integer width, Integer height, String background, String color, Integer fontSize, Shape shape) {
        this.tag = tag;
        this.width = width;
        this.height = height;
        this.background = background;
        this.color = color;
        this.fontSize = fontSize;
        this.shape = shape;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
