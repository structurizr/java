package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public abstract class Element {

    private Model model;
    protected long id = -1;

    protected String name;
    protected String description;
    protected Location location = Location.Internal;

    protected Set<Relationship> relationships = new HashSet<>();

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract ElementType getType();

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
        this.location = location;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    @JsonIgnore
    public Key getKey() {
        return new Key(getType(), id);
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }
}
