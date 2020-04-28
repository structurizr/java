package com.structurizr.model;

/**
 * Defines the interface for strategies to create implied relationships in the model,
 * after a relationship has been created.
 */
public interface ImpliedRelationshipsStrategy {

    /**
     * Called after a relationship has been created in the model,
     * providing an opportunity to create any resulting implied relationships.
     *
     * @param relationship      the newly created Relationship
     */
    void createImpliedRelationships(Relationship relationship);

}