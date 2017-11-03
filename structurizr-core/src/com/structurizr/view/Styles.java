package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

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

    private ElementStyle findElementStyle(String tag) {
        if (tag != null) {
            for (ElementStyle elementStyle : elements) {
                if (elementStyle != null && elementStyle.getTag().equals(tag)) {
                    return elementStyle;
                }
            }
        }

        return null;
    }

    private RelationshipStyle findRelationshipStyle(String tag) {
        if (tag != null) {
            for (RelationshipStyle relationshipStyle : relationships) {
                if (relationshipStyle != null && relationshipStyle.getTag().equals(tag)) {
                    return relationshipStyle;
                }
            }
        }

        return null;
    }

    public ElementStyle findElementStyle(Element element) {
        ElementStyle style = new ElementStyle("").background("#dddddd").color("#000000").shape(Shape.Box);

        if (element != null) {
            for (String tag : element.getTagsAsSet()) {
                ElementStyle elementStyle = findElementStyle(tag);
                if (elementStyle != null) {
                    if (elementStyle.getBackground() != null && elementStyle.getBackground().trim().length() > 0) {
                        style.setBackground(elementStyle.getBackground());
                    }

                    if (elementStyle.getColor() != null && elementStyle.getColor().trim().length() > 0) {
                        style.setColor(elementStyle.getColor());
                    }

                    if (elementStyle.getShape() != null) {
                        style.setShape(elementStyle.getShape());
                    }
                }
            }
        }

        return style;
    }

    public RelationshipStyle findRelationshipStyle(Relationship relationship) {
        RelationshipStyle style = new RelationshipStyle("").color("#707070");

        if (relationship != null) {
            for (String tag : relationship.getTagsAsSet()) {
                RelationshipStyle relationshipStyle = findRelationshipStyle(tag);
                if (relationshipStyle != null) {
                    if (relationshipStyle.getColor() != null && relationshipStyle.getColor().trim().length() > 0) {
                        style.setColor(relationshipStyle.getColor());
                    }
                }
            }
        }

        return style;
    }

}
