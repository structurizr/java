package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

/**
 * A view that has been rendered elsewhere (e.g. PlantUML, Mermaid, Kroki, etc) as a image (e.g. PNG).
 */
public final class ImageView extends View {

    private Element element;
    private String elementId;
    private String content;
    private String contentLight;
    private String contentDark;
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
    public String getContent() {
        return content;
    }

    /**
     * Gets the content of this view (a URL or a data URI), for the light color scheme.
     *
     * @return  the content, as a String
     */
    public String getContentLight() {
        return contentLight;
    }

    /**
     * Gets the content of this view (a URL or a data URI), for the dark color scheme.
     *
     * @return  the content, as a String
     */
    public String getContentDark() {
        return contentDark;
    }

    /**
     * Sets the content of this image view, which needs to be a URL or a data URI.
     *
     * @param content   the content of this view
     */
    public void setContent(String content) {
        setContent(content, null);
    }

    /**
     * Sets the content of this image view, which needs to be a URL or a data URI.
     *
     * @param content   the content of this view
     */
    public void setContent(String content, ColorScheme colorScheme) {
        if (StringUtils.isNullOrEmpty(content)) {
            if (colorScheme == ColorScheme.Dark) {
                this.contentDark = null;
            } else if (colorScheme == ColorScheme.Light) {
                this.contentLight = null;
            } else {
                this.content = null;
            }
        } else {
            ImageUtils.validateImage(content);
            content = content.trim();

            if (colorScheme == ColorScheme.Dark) {
                this.contentDark = content;
            } else if (colorScheme == ColorScheme.Light) {
                this.contentLight = content;
            } else {
                this.content = content;
            }
        }
    }

    /**
     * Gets the content type of this view (e.g. "image/png").
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

    public boolean hasContent() {
        return
                !StringUtils.isNullOrEmpty(content) ||
                !StringUtils.isNullOrEmpty(contentLight) ||
                !StringUtils.isNullOrEmpty(contentDark);
    }

}