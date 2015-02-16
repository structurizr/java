package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class Element extends TaggableThing {

    // TODO: what happens if an element name includes a forward slash character?
    public static final String CANONICAL_NAME_SEPARATOR = "/";

    private Model model;
    protected String id = "";

    protected String name;
    protected String description;

    protected Set<Relationship> relationships = new LinkedHashSet<>();

    protected Element() {
        addTags(Tags.ELEMENT);
    }

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

    @JsonIgnore
    public abstract String getCanonicalName();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Element)) {
            return false;
        }

        Element element = (Element)o;
        return getCanonicalName().equals(element.getCanonicalName());
    }

}
