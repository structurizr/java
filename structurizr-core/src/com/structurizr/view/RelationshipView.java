package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Relationship;

import java.util.Collection;
import java.util.LinkedList;

public class RelationshipView {

    private Relationship relationship;
    private String id;
    private Collection<Vertex> vertices = new LinkedList<>();

    RelationshipView() {
    }

    public RelationshipView(Relationship relationship) {
        this.relationship = relationship;
    }

    public String getId() {
        if (relationship != null) {
            return relationship.getId();
        } else {
            return this.id;
        }
    }

    void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public Collection<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Collection<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void copyLayoutInformationFrom(RelationshipView source) {
        if (source != null) {
            setVertices(source.getVertices());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipView that = (RelationshipView) o;

        if (!getId().equals(that.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return relationship.toString();
    }

}
