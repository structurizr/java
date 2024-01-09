package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

/**
 * A definition of an element style.
 */
public final class ElementStyle extends AbstractStyle {

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
    private String stroke;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer strokeWidth;

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

    public void setBackground(String color) {
        if (Color.isHexColorCode(color)) {
            this.background = color.toLowerCase();
        } else {
            String hexColorCode = Color.fromColorNameToHexColorCode(color);

            if (hexColorCode != null) {
                this.background = hexColorCode.toLowerCase();
            } else {
                throw new IllegalArgumentException(color + " is not a valid hex colour code or HTML colour name.");
            }
        }
    }

    public ElementStyle background(String background) {
        setBackground(background);
        return this;
    }

    /**
     * Gets the stroke colour of the element, as a HTML RGB hex string (e.g. #123456).
     *
     * @return  the stroke colour as a String, or null if not specified
     */
    public String getStroke() {
        return stroke;
    }

    public void setStroke(String color) {
        if (Color.isHexColorCode(color)) {
            this.stroke = color.toLowerCase();
        } else {
            String hexColorCode = Color.fromColorNameToHexColorCode(color);

            if (hexColorCode != null) {
                this.stroke = hexColorCode.toLowerCase();
            } else {
                throw new IllegalArgumentException(color + " is not a valid hex colour code or HTML colour name.");
            }
        }
    }

    public ElementStyle stroke(String color) {
        setStroke(color);
        return this;
    }

    /**
     * Gets the stroke width, in pixels, between 1 and 10.
     *
     * @return  the stroke width
     */
    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        if (strokeWidth == null) {
            this.strokeWidth = null;
        } else if (strokeWidth < 1) {
            this.strokeWidth = 1;
        } else {
            this.strokeWidth = Math.min(10, strokeWidth);
        }
    }

    public ElementStyle strokeWidth(Integer strokeWidth) {
        setStrokeWidth(strokeWidth);
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
            String hexColorCode = Color.fromColorNameToHexColorCode(color);

            if (hexColorCode != null) {
                this.color = hexColorCode.toLowerCase();
            } else {
                throw new IllegalArgumentException(color + " is not a valid hex colour code or HTML colour name.");
            }
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
        if (StringUtils.isNullOrEmpty(icon)) {
            this.icon = null;
        } else {
            ImageUtils.validateImage(icon);
            this.icon = icon.trim();
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

    void copyFrom(ElementStyle elementStyle) {
        if (elementStyle.getWidth() != null) {
            this.setWidth(elementStyle.getWidth());
        }

        if (elementStyle.getHeight() != null) {
            this.setHeight(elementStyle.getHeight());
        }

        if (!StringUtils.isNullOrEmpty(elementStyle.getBackground())) {
            this.setBackground(elementStyle.getBackground());
        }

        if (!StringUtils.isNullOrEmpty(elementStyle.getStroke())) {
            this.setStroke(elementStyle.getStroke());
        }

        if (elementStyle.getStrokeWidth() != null) {
            this.setStrokeWidth(elementStyle.getStrokeWidth());
        }

        if (!StringUtils.isNullOrEmpty(elementStyle.getColor())) {
            this.setColor(elementStyle.getColor());
        }

        if (elementStyle.getFontSize() != null) {
            this.setFontSize(elementStyle.getFontSize());
        }

        if (elementStyle.getShape() != null) {
            this.setShape(elementStyle.getShape());
        }

        if (!StringUtils.isNullOrEmpty(elementStyle.getIcon())) {
            this.setIcon(elementStyle.getIcon());
        }

        if (elementStyle.getBorder() != null) {
            this.setBorder(elementStyle.getBorder());
        }

        if (elementStyle.getOpacity() != null) {
            this.setOpacity(elementStyle.getOpacity());
        }

        if (elementStyle.getMetadata() != null) {
            this.setMetadata(elementStyle.getMetadata());
        }

        if (elementStyle.getDescription() != null) {
            this.setDescription(elementStyle.getDescription());
        }

        for (String name : elementStyle.getProperties().keySet()) {
            this.addProperty(name, elementStyle.getProperties().get(name));
        }
    }
    
}