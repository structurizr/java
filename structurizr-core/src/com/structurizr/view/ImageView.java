package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

import javax.annotation.Nullable;

/**
 * A view that has been rendered elsewhere (e.g. PlantUML, Mermaid, Kroki, etc) as a image (e.g. PNG).
 */
public final class ImageView extends View {

    private Element element;
    private String elementId;
    @Nullable
    private String content;
    private String contentType;

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
    @Nullable
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of this image view, which needs to be a URL or a data URI.
     *
     * @param content   the content of this view
     */
    public void setContent(@Nullable String content) {
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

    @Override
    public String getName() {
        return getTitle();
    }

}
