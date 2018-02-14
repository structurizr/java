package com.structurizr.documentation;

/**
 * Represents some basic metadata about the documentation template being used.
 */
public class TemplateMetadata {

    private String name;
    private String author;
    private String url;

    TemplateMetadata() {
    }

    public TemplateMetadata(String name, String author, String url) {
        this.name = name;
        this.author = author;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

}