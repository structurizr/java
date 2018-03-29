package com.structurizr.model;

/**
 * The interface that ID generators, used when creating IDs for model elements, must implement.
 */
public interface IdGenerator {

    /**
     * Generates an ID for the specified model element.
     *
     * @param element       an Element instance
     * @return      the ID, as a String
     */
    String generateId(Element element);

    /**
     * Generates an ID for the specified model relationship.
     *
     * @param relationship      a Relationship instance
     * @return      the ID, as a String
     */
    String generateId(Relationship relationship);

    /**
     * Called when loading/deserializing a model, to indicate that the specified ID has been found
     * (and shouldn't be reused when generating new IDs).
     *
     * @param id        the ID that has been found
     */
    void found(String id);

}