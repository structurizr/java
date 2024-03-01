package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.Set;
import java.util.TreeSet;

/**
 * A wrapper for a collection of animation steps.
 */
public final class Animation {

    private int order;
    private Set<String> elements = new TreeSet<>();
    private Set<String> relationships = new TreeSet<>();

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
        return new TreeSet<>(elements);
    }

    void setElements(Set<String> elements) {
        if (elements != null) {
            this.elements = new TreeSet<>(elements);
        }
    }

    public Set<String> getRelationships() {
        return new TreeSet<>(relationships);
    }

    void setRelationships(Set<String> relationships) {
        if (relationships != null) {
            this.relationships = new TreeSet<>(relationships);
        }
    }

}