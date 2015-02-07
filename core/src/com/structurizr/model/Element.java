package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.Collections;
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

    protected Set<String> tags = new LinkedHashSet<>();

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

    public String getTags() {
        if (this.tags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : tags) {
            buf.append(tag);
            buf.append(",");
        }

        String tagsAsString = buf.toString();
        return tagsAsString.substring(0, tagsAsString.length()-1);
    }

    void setTags(String tags) {
        if (tags == null) {
            return;
        }

        this.tags.clear();
        Collections.addAll(this.tags, tags.split(","));
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag);
            }
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
