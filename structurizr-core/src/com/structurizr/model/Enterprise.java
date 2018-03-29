package com.structurizr.model;

/**
 * Represents an "enterprise" (e.g. an organisation, a department, etc).
 */
public final class Enterprise {

    private String name;

    Enterprise() {
    }

    /**
     * Creates a new enterprise with the specified name.
     *
     * @param name      the name, as a String
     * @throws IllegalArgumentException     if the name is not specified
     */
    public Enterprise(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name must be specified.");
        }

        this.name = name;
    }

    /**
     * Gets the name of this enterprise.
     *
     * @return      the name, as a String
     */
    public String getName() {
        return name;
    }

}