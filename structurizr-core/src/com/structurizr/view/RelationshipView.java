package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Relationship;

import java.util.Collection;
import java.util.LinkedList;

public class RelationshipView {

    private Relationship relationship;
    private String id;
    private String description;
    private String order;
    private Collection<Vertex> vertices = new LinkedList<>();

    RelationshipView() {
    }

    RelationshipView(Relationship relationship) {
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

    /**
     * Gets the description of this relationship (used in dynamic views only).
     *
     * @return  the description, as a String
     *          or an empty string if a description has not been set
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the order of this relationship (used in dynamic views only; e.g. 1.0, 1.1, 2.0, etc).
     *
     * @return  the order, as a String
     */
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Gets the set of vertices used to render the relationship.
     *
     * @return  a collection of Vertex objects
     */
    public Collection<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Collection<Vertex> vertices) {
        this.vertices = vertices;
    }

    void copyLayoutInformationFrom(RelationshipView source) {
        if (source != null) {
            setVertices(source.getVertices());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipView that = (RelationshipView) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!getId().equals(that.getId())) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return relationship != null ? relationship.toString() : "";
    }

}
