package com.structurizr.model;

/**
 * Represents an architectural perspective, that can be applied to elements and relationships.
 * See https://www.viewpoints-and-perspectives.info/home/perspectives/ for more details of this concept.
 */
public final class Perspective {

    private String name;
    private String description;
    private String value;

    Perspective() {
    }

    Perspective(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    /**
     * Gets the name of this perspective (e.g. "Security").
     *
     * @return  the name of this perspective, as a String
     */
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of this perspective.
     *
     * @return  the description of this perspective, as a String
     */
    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the value of this perspective.
     *
     * @return  the value of this perspective, as a String
     */
    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Perspective that = (Perspective) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

}