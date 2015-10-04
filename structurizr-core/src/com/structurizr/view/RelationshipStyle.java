package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RelationshipStyle {

    private static final int START_OF_LINE = 0;
    private static final int END_OF_LINE = 100;

    /** the name of the tag to which this style applies */
    private String tag;

    /** the thickness of the line, in pixels */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer thickness;

    /** the colour of the line, as a HTML hex value (e.g. #123456) */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String color;

    /** the font size of the annotation, in pixels */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer fontSize;

    /** the width of the annotation, in pixels */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer width;

    /** whether the line should be dashed or not */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean dashed;

    /** whether the line should be smooth or not */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean smooth;

    /** the position of the annotation along the line; 0 (start) to 100 (end) */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer position;

    public RelationshipStyle() {
    }

    public RelationshipStyle(String tag) {
        this.tag = tag;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public RelationshipStyle(String tag, Integer thickness, String color, Boolean dashed, Integer fontSize, Integer width, Integer position) {
        this(tag, thickness, color, dashed, false, fontSize, width, position);
    }

    public RelationshipStyle(String tag, Integer thickness, String color, Boolean dashed, Boolean smooth, Integer fontSize, Integer width, Integer position) {
        this.tag = tag;
        this.thickness = thickness;
        this.color = color;
        this.dashed = dashed;
        this.smooth = smooth;
        this.fontSize = fontSize;
        this.width = width;
        this.position = position;
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

    public Boolean getDashed() {
        return dashed;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public Boolean getSmooth() {
        return smooth;
    }

    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        if (position == null) {
            this.position = null;
        } else if (position < START_OF_LINE) {
            this.position = START_OF_LINE;
        } else if (position > END_OF_LINE) {
            this.position = END_OF_LINE;
        } else {
            this.position = position;
        }
    }

}
