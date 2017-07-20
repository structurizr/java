package com.structurizr.view;

import java.util.Collection;
import java.util.LinkedList;

public final class Styles {

    private Collection<ElementStyle> elements = new LinkedList<>();
    private Collection<RelationshipStyle> relationships = new LinkedList<>();

    public Collection<ElementStyle> getElements() {
        return elements;
    }

    public void add(ElementStyle elementStyle) {
        if (elementStyle != null) {
            this.elements.add(elementStyle);
        }
    }

    public ElementStyle addElementStyle(String tag) {
        ElementStyle elementStyle = null;

        if (tag != null) {
            elementStyle = new ElementStyle();
            elementStyle.setTag(tag);
            add(elementStyle);
        }

        return elementStyle;
    }

    public Collection<RelationshipStyle> getRelationships() {
        return relationships;
    }

    public void add(RelationshipStyle relationshipStyle) {
        if (relationshipStyle != null) {
            this.relationships.add(relationshipStyle);
        }
    }

    public RelationshipStyle addRelationshipStyle(String tag) {
        RelationshipStyle relationshipStyle = null;

        if (tag != null) {
            relationshipStyle = new RelationshipStyle();
            relationshipStyle.setTag(tag);
             add(relationshipStyle);
        }

        return relationshipStyle;
    }

}
