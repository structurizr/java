package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

/**
 * A view that has been rendered elsewhere (e.g. PlantUML, Mermaid, Kroki, etc) as a image (e.g. PNG).
 */
public final class ImageView {

    private String key;
    private int order;

    private Element element;
    private String elementId;
    private String content;
    private String contentType;

    private String title;
    private String description;

    ImageView() {
    }

    ImageView(String key) {
        setKey(key);
    }

    ImageView(Element element, String key) {
        this(key);
        setElement(element);
    }

    /**
     * Gets the ID of the element associated with this view.
     *
     * @return the ID, as a String, or null if not set
     */
    public String getElementId() {
        if (this.element != null) {
            return element.getId();
        } else {
            return this.elementId;
        }
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the content of this view (a URL or a data URI).
     *
     * @return  the content, as a String
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of this image view, which needs to be a URL or a data URI.
     *
     * @param content   the content of this view
     */
    public void setContent(String content) {
        if (StringUtils.isNullOrEmpty(content)) {
            this.content = null;
        } else {
            ImageUtils.validateImage(content);
            this.content = content.trim();
        }
    }

    /**
     * Gets the the content type of this view (e.g. "image/png").
     *
     * @return      the content type, as a String
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type of this view (e.g. "image/png").
     *
     * @param contentType   the content type, as a String
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the order of this view.
     *
     * @return  a positive integer
     */
    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = Math.max(1, order);
    }

    /**
     * Gets the title of this view, if one has been set.
     *
     * @return  the title, as a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for this view.
     *
     * @param title     the title, as a String
     */
    public void setTitle(String title) {
        if (StringUtils.isNullOrEmpty(title)) {
            throw new IllegalArgumentException("A title must be specified");
        }
        this.title = title;
    }

    /**
     * Gets the description of this view.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this view.
     *
     * @param description       the description, as a string
     */
    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    /**
     * Gets the identifier for this view.
     *
     * @return the identifier, as a String
     */
    public String getKey() {
        return key;
    }

    void setKey(String key) {
        if (key != null) {
            key = key.replaceAll("/", "_");
        }

        this.key = key;
    }

}