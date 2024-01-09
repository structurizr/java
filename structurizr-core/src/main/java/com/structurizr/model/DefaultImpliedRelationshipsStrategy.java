package com.structurizr.model;

/**
 * The default strategy is to NOT create implied relationships.
 */
public class DefaultImpliedRelationshipsStrategy extends AbstractImpliedRelationshipsStrategy {

    @Override
    public void createImpliedRelationships(Relationship relationship) {
        // do nothing
    }

}