package com.structurizr.model;

/**
 * This provides a way to uniquely identify elements given their type and ID.
 */
public class Key {

    private static final String SEPARATOR = "_";

    private ElementType type;
    private long id;

    public Key(ElementType type, long id) {
        this.type = type;
        this.id = id;
    }

    public Key(String keyAsString) {
        String[] parts = keyAsString.split(SEPARATOR);
        this.type = ElementType.valueOf(parts[0]);
        this.id = Long.parseLong(parts[1]);
    }

    public ElementType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return type.toString() + SEPARATOR + id;
    }

}
