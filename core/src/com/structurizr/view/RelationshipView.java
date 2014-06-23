package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Relationship;

public class RelationshipView {

    private Relationship relationship;
    private int sourceId;
    private int destinationId;

    public RelationshipView() {
    }

    public RelationshipView(Relationship relationship) {
        this.relationship = relationship;
    }

    @JsonIgnore
    public Relationship getRelationship() {
        return relationship;
    }

    public int getSourceId() {
        if (relationship != null) {
            return relationship.getSourceId();
        } else {
            return this.sourceId;
        }
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDestinationId() {
        if (relationship != null) {
            return relationship.getDestinationId();
        } else {
            return this.destinationId;
        }
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public String toString() {
        return relationship.toString();
    }

}
