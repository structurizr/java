package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Collection;
import java.util.LinkedList;

final class Theme {

    private String name;
    private String description;
    private Collection<ElementStyle> elements = new LinkedList<>();
    private Collection<RelationshipStyle> relationships = new LinkedList<>();

    Theme() {
    }

    Theme(Collection<ElementStyle> elements, Collection<RelationshipStyle> relationships) {
        this.elements = elements;
        this.relationships = relationships;
    }

    Theme(String name, String description, Collection<ElementStyle> elements, Collection<RelationshipStyle> relationships) {
        this.name = name;
        this.description = description;
        this.elements = elements;
        this.relationships = relationships;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @JsonGetter
    Collection<ElementStyle> getElements() {
        return elements;
    }

    void setElements(Collection<ElementStyle> elements) {
        this.elements = elements;
    }

    @JsonGetter
    Collection<RelationshipStyle> getRelationships() {
        return relationships;
    }

    void setRelationships(Collection<RelationshipStyle> relationships) {
        this.relationships = relationships;
    }

}