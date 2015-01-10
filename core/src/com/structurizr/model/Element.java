package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class Element {

    private Model model;
    protected String id = "";

    protected String name;
    protected String description;
    protected Location location = Location.Unspecified;

    protected Set<Relationship> relationships = new LinkedHashSet<>();

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    boolean has(Relationship relationship) {
        return relationships.contains(relationship);
    }

    void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    public Set<Relationship> getRelationships() {
        return new LinkedHashSet<>(relationships);
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    public abstract ElementType getType();

}
