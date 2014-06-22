package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Relationship;

public class RelationshipView {

    private Relationship relationship;

    public RelationshipView(Relationship relationship) {
        this.relationship = relationship;
    }

    @JsonIgnore
    public Relationship getRelationship() {
        return relationship;
    }

    public long getSourceId() {
        return relationship.getSourceId();
    }

    public long getDestinationId() {
        return relationship.getDestinationId();
    }

    @Override
    public String toString() {
        return relationship.toString();
    }

}
