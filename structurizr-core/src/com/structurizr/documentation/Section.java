package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

/**
 * A documentation section.
 */
public final class Section {

    private Element element;
    private String elementId;

    private String type;
    private String title;
    private int order;
    private Format format;
    private String content;

    Section() {
    }

    Section(Element element, String title, int order, Format format, String content) {
        this.element = element;
        this.title = title;
        this.order = order;
        this.format = format;
        this.content = content;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    void setElement(Element element) {
        this.element = element;
    }

    public String getElementId() {
        if (this.element != null) {
            return this.element.getId();
        } else {
            return elementId;
        }
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter
    String getType() {
        return this.type;
    }

    void setType(String type) {
        this.type = type;
        setTitle(type); // backwards compatibility for older clients
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Format getFormat() {
        return format;
    }

    void setFormat(Format format) {
        this.format = format;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Section section = (Section)object;
        if (getElementId() != null) {
            return getElementId().equals(section.getElementId()) && getTitle().equals(section.getTitle());
        } else {
            return getTitle().equals(section.getTitle());
        }
    }

    @Override
    public int hashCode() {
        int result = getElementId() != null ? getElementId().hashCode() : 0;
        result = 31 * result + getTitle().hashCode();
        return result;
    }

}