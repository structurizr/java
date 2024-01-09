package com.structurizr.documentation;

/**
 * Represents a piece of documentation content ... a section or a decision.
 */
public abstract class DocumentationContent {

    // elementId is here for backwards compatibility
    private String elementId;

    private String content;
    private Format format;

    DocumentationContent() {
    }

    /**
     * Gets the ID of the element that this documentation content is associated with.
     * Please note this is unused, and only here for backwards compatibility.
     *
     * @return      the element ID, as a String
     */
    public String getElementId() {
        return elementId;
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    /**
     * Gets the content.
     *
     * @return      the content, as a String
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the format of this content.
     *
     * @return      Markdown or AsciiDoc
     */
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

}