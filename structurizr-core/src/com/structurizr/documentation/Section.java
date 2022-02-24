package com.structurizr.documentation;

/**
 * A documentation section.
 */
public final class Section extends DocumentationContent {

    private String title;
    private int order;
    private Format format;
    private String content;

    public Section() {
    }

    public Section(String title, Format format, String content) {
        setTitle(title);
        setFormat(format);
        setContent(content);
    }

    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
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