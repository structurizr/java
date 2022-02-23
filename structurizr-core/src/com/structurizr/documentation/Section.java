package com.structurizr.documentation;

/**
 * A documentation section.
 */
public final class Section {

    // elementId is here for backwards compatibility
    private String elementId;

    private String title;
    private int order;
    private Format format;
    private String content;

    Section() {
    }

    Section(String title, Format format, String content) {
        this.title = title;
        this.format = format;
        this.content = content;
    }

    public String getElementId() {
        return elementId;
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

    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
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

    public void setContent(String content) {
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