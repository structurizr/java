package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Relationship;

public class RelationshipView {

    private Relationship relationship;
    private String id;

    public RelationshipView() {
    }

    public RelationshipView(Relationship relationship) {
        this.relationship = relationship;
    }

    @JsonIgnore
    public Relationship getRelationship() {
        return relationship;
    }

    public String getId() {
        if (relationship != null) {
            return relationship.getId();
        } else {
            return this.id;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return relationship.toString();
    }

}
