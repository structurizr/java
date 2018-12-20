package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.structurizr.util.Url;

/**
 * A definition of an element style.
 */
public final class ElementStyle {

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

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String icon;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Border border;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer opacity;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean metadata;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean description;

    ElementStyle() {
    }

    ElementStyle(String tag) {
        this.tag = tag;
    }

    public ElementStyle(String tag, Integer width, Integer height, String background, String color, Integer fontSize) {
        this(tag, width, height, background, color, fontSize, null);
    }

    public ElementStyle(String tag, Integer width, Integer height, String background, String color, Integer fontSize, Shape shape) {
        this.tag = tag;
        this.width = width;
        this.height = height;
        setBackground(background);
        setColor(color);
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

    public ElementStyle width(int width) {
        setWidth(width);
        return this;
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

    public ElementStyle height(int height) {
        setHeight(height);
        return this;
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
        if (Color.isHexColorCode(background)) {
            this.background = background.toLowerCase();
        } else {
            throw new IllegalArgumentException(background + " is not a valid hex colour code.");
        }
    }

    public ElementStyle background(String background) {
        setBackground(background);
        return this;
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
        if (Color.isHexColorCode(color)) {
            this.color = color.toLowerCase();
        } else {
            throw new IllegalArgumentException(color + " is not a valid hex colour code.");
        }
    }

    public ElementStyle color(String color) {
        setColor(color);
        return this;
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

    public ElementStyle fontSize(int fontSize) {
        setFontSize(fontSize);
        return this;
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

    public ElementStyle shape(Shape shape) {
        setShape(shape);
        return this;
    }

    /**
     * Gets the icon of the element (a URL, or a data URI representing a Base64 encoded PNG/JPG/GIF file).
     *
     * @return  the icon, or null if not specified
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        if (icon != null && icon.trim().length() > 0) {
            if (Url.isUrl(icon) || icon.startsWith("data:image/")) {
                this.icon = icon.trim();
            } else {
                throw new IllegalArgumentException(icon + " is not a valid URL.");
            }
        }
    }

    public ElementStyle icon(String icon) {
        setIcon(icon);
        return this;
    }

    /**
     * Gets the border used when rendering the element.
     *
     * @return  a Border, or null if not specified
     */
    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public ElementStyle border(Border border) {
        setBorder(border);
        return this;
    }

    /**
     * Gets the opacity used when rendering the element.
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

    public ElementStyle opacity(int opacity) {
        setOpacity(opacity);
        return this;
    }

    /**
     * Determines whether the element metadata should be shown or not.
     *
     * @return  true (shown), false (hidden) or null (not set)
     */
    public Boolean getMetadata() {
        return metadata;
    }

    /**
     * Sets whether the element metadata should be shown or not.
     *
     * @param metadata  true (shown), false (hidden) or null (not set)
     */
    public void setMetadata(Boolean metadata) {
        this.metadata = metadata;
    }

    public ElementStyle metadata(boolean metadata) {
        setMetadata(metadata);
        return this;
    }

    /**
     * Determines whether the element description should be shown or not.
     *
     * @return  true (shown), false (hidden) or null (not set)
     */
    public Boolean getDescription() {
        return description;
    }

    /**
     * Sets whether the element description should be shown or not.
     *
     * @param description   true (shown), false (hidden) or null (not set)
     */
    public void setDescription(Boolean description) {
        this.description = description;
    }

    public ElementStyle description(boolean description) {
        setDescription(description);
        return this;
    }

}