package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

/**
 * A documentation section.
 */
public final class Section {

    private Element element;
    private String elementId;

    private String type;
    private int order;
    private int group;
    private Format format;
    private String content;

    Section() {
    }

    Section(Element element, String type, int order, int group, Format format, String content) {
        this.element = element;
        this.type = type;
        this.order = order;
        this.group = group;
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

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getGroup() {
        return group;
    }

    void setGroup(int group) {
        this.group = group;
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
            return getElementId().equals(section.getElementId()) && getType().equals(section.getType());
        } else {
            return getType().equals(section.getType());
        }
    }

    @Override
    public int hashCode() {
        int result = getElementId() != null ? getElementId().hashCode() : 0;
        result = 31 * result + getType().hashCode();
        return result;
    }

}