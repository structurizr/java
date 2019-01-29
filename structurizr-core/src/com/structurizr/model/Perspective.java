package com.structurizr.model;

/**
 * Represents an architectural perspective, that can be applied to elements and relationships.
 * See https://www.viewpoints-and-perspectives.info/home/perspectives/ for more details of this concept.
 */
public final class Perspective {

    private String name;
    private String description;
    // todo link this perspective to architecture decision records

    Perspective() {
    }

    Perspective(String name, String description) {
        this.name = name;
        this.description = description;
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