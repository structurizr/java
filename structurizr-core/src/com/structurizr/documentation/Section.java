package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

/**
 * A documentation section.
 */
public final class Section {

    private Element element;
    private String elementId;

    private Type type;
    private Format format;
    private String content;

    Section() {
    }

    Section(Element element, Type type, Format format, String content) {
        this.element = element;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    void setType(Type type) {
        this.type = type;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        return getElementId().equals(section.getElementId()) && type == section.type;
    }

    @Override
    public int hashCode() {
        int result = getElementId().hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

}
