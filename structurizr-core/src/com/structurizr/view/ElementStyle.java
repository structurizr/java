package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A definition of an element style.
 */
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

    /**
     * The tag to which this element style applies.
     *
     * @return  the tag, as a String
     */
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the width of the element, in pixels.
     *
     * @return  the width as an Integer, or null if not specified
     */
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Gets the height of the element, in pixels.
     *
     * @return  the height as an Integer, or null if not specified
     */
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Gets the background colour of the element, as a HTML RGB hex string (e.g. #123456).
     *
     * @return  the background colour as a String, or null if not specified
     */
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * Gets the foreground (text) colour of the element, as a HTML RGB hex string (e.g. #123456).
     *
     * @return  the foreground colour as a String, or null if not specified
     */
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the standard font size used to render text, in pixels.
     *
     * @return  the font size, in pixels, as an Integer, or null if not specified
     */
    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Gets the shape used to render the element.
     *
     * @return  a Shape, or null if not specified
     */
    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
