package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

public final class RelationshipStyle {

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

    /** the routing algorithm used when rendering lines */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Routing routing;

    /** the position of the annotation along the line; 0 (start) to 100 (end) */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer position;

    /** the opacity of the line/text; 0 to 100 */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer opacity;

    RelationshipStyle() {
    }

    RelationshipStyle(String tag) {
        this.tag = tag;
    }

    public RelationshipStyle(String tag, Integer thickness, String color, Boolean dashed, Routing routing, Integer fontSize, Integer width, Integer position) {
        this.tag = tag;
        this.thickness = thickness;
        setColor(color);
        this.dashed = dashed;
        this.routing = routing;
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

    public RelationshipStyle thickness(int thickness) {
        setThickness(thickness);
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (Color.isHexColorCode(color)) {
            this.color = color.toLowerCase();
        } else {
            throw new IllegalArgumentException(color + " is not a valid hex colour code.");
        }
    }

    public RelationshipStyle color(String color) {
        setColor(color);
        return this;
    }

    public Boolean getDashed() {
        return dashed;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public RelationshipStyle dashed(boolean dashed) {
        setDashed(dashed);
        return this;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    public RelationshipStyle routing(Routing routing) {
        setRouting(routing);
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public RelationshipStyle fontSize(int fontSize) {
        setFontSize(fontSize);
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public RelationshipStyle width(int width) {
        setWidth(width);
        return this;
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

    public RelationshipStyle position(int position) {
        setPosition(position);
        return this;
    }

    /**
     * Gets the opacity used when rendering the relationship.
     *
     * @return  the opacity, as an integer between 0 and 100.
     */
    public Integer getOpacity() {
        return opacity;
    }

    public void setOpacity(Integer opacity) {
        if (opacity != null) {
            if (opacity < 0) {
                this.opacity = 0;
            } else if (opacity > 100) {
                this.opacity = 100;
            } else {
                this.opacity = opacity;
            }
        }
    }

    public RelationshipStyle opacity(int opacity) {
        setOpacity(opacity);
        return this;
    }

}
