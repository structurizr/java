package com.structurizr.documentation;

/**
 * A documentation section.
 */
public final class Section extends DocumentationContent {

    private String filename;
    private int order;

    public Section() {
    }

    public Section(String title, Format format, String content) {
        setTitle(title);
        setFormat(format);
        setContent(content);
    }

    /**
     * Gets the filename of this section.
     *
     * @return  the filename, as a String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename of this section (e.g. where this section was imported from).
     *
     * @param filename      the filename, as a String
     */
    public void setFilename(String filename) {
        this.filename = filename;
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