package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A documentation section.
 */
public final class Section extends DocumentationContent {

    private String filename;
    private int order;

    public Section() {
    }

    public Section(Format format, String content) {
        setFormat(format);
        setContent(content);
    }

    /**
     * This method is retained for backwards compatibility.
     */
    @JsonGetter
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public String getTitle() {
        return "";
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

}