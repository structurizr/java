package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ElementStyle {

    private String tag;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer width;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer height;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String background;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String color;

    public ElementStyle() {
    }

    public ElementStyle(String tag, Integer width, Integer height, String background, String color) {
        this.tag = tag;
        this.width = width;
        this.height = height;
        this.background = background;
        this.color = color;
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

}
