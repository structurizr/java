package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class Element {

    private Model model;
    protected int id = -1;

    protected String name;
    protected String description;
    protected Location location = Location.Unspecified;

    protected Set<Relationship> relationships = new HashSet<>();

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    protected void addRelationship(Relationship relationship) {
        if (!relationships.contains(relationship)) {
            relationships.add(relationship);
        }
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    public abstract ElementType getType();

}
