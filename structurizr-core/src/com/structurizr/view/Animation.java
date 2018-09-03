package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * A wrapper for a collection of animation steps.
 */
final class Animation {

    private int order;
    private Set<String> elements = new HashSet<>();
    private Set<String> relationships = new HashSet<>();

    Animation() {
    }

    Animation(int order, Set<Element> elements, Set<Relationship> relationships) {
        this.order = order;

        for (Element element : elements) {
            this.elements.add(element.getId());
        }

        for (Relationship relationship : relationships) {
            this.relationships.add(relationship.getId());
        }
    }

    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
    }

    public Set<String> getElements() {
        return new HashSet<>(elements);
    }

    void setElements(Set<String> elements) {
        if (elements != null) {
            this.elements = new HashSet<>(elements);
        }
    }

    public Set<String> getRelationships() {
        return new HashSet<>(relationships);
    }

    void setRelationships(Set<String> relationships) {
        if (relationships != null) {
            this.relationships = new HashSet<>(relationships);
        }
    }

}