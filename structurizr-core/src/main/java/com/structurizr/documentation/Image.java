package com.structurizr.documentation;

/**
 * Represents a base64 encoded image (png/jpg/gif).
 */
public final class Image implements Comparable<Image> {

    private String name;
    private String content;
    private String type;

    Image() {
    }

    public Image(String name, String type, String content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Image image) {
        return getName().compareTo(image.getName());
    }

}