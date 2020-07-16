package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.model.Tags;
import com.structurizr.util.StringUtils;

import java.util.Collection;
import java.util.LinkedList;

public final class Styles {

    private static final Integer DEFAULT_WIDTH_OF_ELEMENT = 450;
    private static final Integer DEFAULT_HEIGHT_OF_ELEMENT = 300;

    private static final Integer DEFAULT_WIDTH_OF_PERSON = 400;
    private static final Integer DEFAULT_HEIGHT_OF_PERSON = 400;

    private Collection<ElementStyle> elements = new LinkedList<>();
    private Collection<RelationshipStyle> relationships = new LinkedList<>();

    public Collection<ElementStyle> getElements() {
        return elements;
    }

    public void add(ElementStyle elementStyle) {
        if (elementStyle != null) {
            if (findElementStyle(elementStyle.getTag()) == null) {
                this.elements.add(elementStyle);
            } else {
                throw new IllegalArgumentException("An element style for the tag \"" + elementStyle.getTag() + "\" already exists.");
            }
        }
    }

    public ElementStyle addElementStyle(String tag) {
        ElementStyle elementStyle = null;

        if (tag != null) {
            if (elements.stream().anyMatch(es -> es.getTag().equals(tag))) {
                throw new IllegalArgumentException("An element style for the tag \"" + tag + "\" already exists.");
            }

            elementStyle = new ElementStyle();
            elementStyle.setTag(tag);
            add(elementStyle);
        }

        return elementStyle;
    }

    /**
     * Removes all element styles.
     */
    public void clearElementStyles() {
        this.elements = new LinkedList<>();
    }

    /**
     * Removes all relationship styles.
     */
    public void clearRelationshipStyles() {
        this.relationships = new LinkedList<>();
    }

    public void addDefaultStyles() {
        addElementStyle(Tags.ELEMENT).shape(Shape.RoundedBox);
        addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        addElementStyle(Tags.CONTAINER).background("#438dd5").color("#ffffff");
        addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000");
        addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        addElementStyle(Tags.INFRASTRUCTURE_NODE).background("#ffffff");
    }

    public Collection<RelationshipStyle> getRelationships() {
        return relationships;
    }

    public void add(RelationshipStyle relationshipStyle) {
        if (relationshipStyle != null) {
            if (findRelationshipStyle(relationshipStyle.getTag()) == null) {
                this.relationships.add(relationshipStyle);
            } else {
                throw new IllegalArgumentException("A relationship style for the tag \"" + relationshipStyle.getTag() + "\" already exists.");
            }
        }
    }

    public RelationshipStyle addRelationshipStyle(String tag) {
        RelationshipStyle relationshipStyle = null;

        if (tag != null) {
            if (relationships.stream().anyMatch(rs -> rs.getTag().equals(tag))) {
                throw new IllegalArgumentException("A relationship style for the tag \"" + tag + "\" already exists.");
            }

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
            tag = tag.trim();
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
                    if (elementStyle.getWidth() != null) {
                        style.setWidth(elementStyle.getWidth());
                    }

                    if (elementStyle.getHeight() != null) {
                        style.setHeight(elementStyle.getHeight());
                    }

                    if (!StringUtils.isNullOrEmpty(elementStyle.getBackground())) {
                        style.setBackground(elementStyle.getBackground());
                    }

                    if (!StringUtils.isNullOrEmpty(elementStyle.getColor())) {
                        style.setColor(elementStyle.getColor());
                    }

                    if (!StringUtils.isNullOrEmpty(elementStyle.getStroke())) {
                        style.setStroke(elementStyle.getStroke());
                    }

                    if (elementStyle.getFontSize() != null) {
                        style.setFontSize(elementStyle.getFontSize());
                    }

                    if (elementStyle.getShape() != null) {
                        style.setShape(elementStyle.getShape());
                    }

                    if (elementStyle.getBorder() != null) {
                        style.setBorder(elementStyle.getBorder());
                    }

                    if (elementStyle.getOpacity() != null) {
                        style.setOpacity(elementStyle.getOpacity());
                    }
                }
            }
        }

        if (style.getWidth() == null) {
            if (style.getShape() == Shape.Person) {
                style.setWidth(DEFAULT_WIDTH_OF_PERSON);
            } else {
                style.setWidth(DEFAULT_WIDTH_OF_ELEMENT);
            }
        }

        if (style.getHeight() == null) {
            if (style.getShape() == Shape.Person) {
                style.setHeight(DEFAULT_HEIGHT_OF_PERSON);
            } else {
                style.setHeight(DEFAULT_HEIGHT_OF_ELEMENT);
            }
        }

        return style;
    }

    public RelationshipStyle findRelationshipStyle(Relationship relationship) {
        RelationshipStyle style = new RelationshipStyle("").color("#707070");
        String tags;

        if (relationship != null) {
            if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
                // the "linked relationship ID" is used for container instance -> container instance relationships
                Relationship linkedRelationship = relationship.getModel().getRelationship(relationship.getLinkedRelationshipId());
                if (linkedRelationship != null) {
                    tags = linkedRelationship.getTags() + "," + relationship.getTags();
                } else {
                    tags = relationship.getTags();
                }
            } else {
                tags = relationship.getTags();
            }

            for (String tag : tags.split(",")) {
                RelationshipStyle relationshipStyle = findRelationshipStyle(tag);
                if (relationshipStyle != null) {
                    if (relationshipStyle.getThickness() != null) {
                        style.setThickness(relationshipStyle.getThickness());
                    }

                    if (!StringUtils.isNullOrEmpty(relationshipStyle.getColor())) {
                        style.setColor(relationshipStyle.getColor());
                    }

                    if (relationshipStyle.getDashed() != null) {
                        style.setDashed(relationshipStyle.getDashed());
                    }

                    if (relationshipStyle.getRouting() != null) {
                        style.setRouting(relationshipStyle.getRouting());
                    }

                    if (relationshipStyle.getFontSize() != null) {
                        style.setFontSize(relationshipStyle.getFontSize());
                    }

                    if (relationshipStyle.getWidth() != null) {
                        style.setWidth(relationshipStyle.getWidth());
                    }

                    if (relationshipStyle.getPosition() != null) {
                        style.setPosition(relationshipStyle.getPosition());
                    }

                    if (relationshipStyle.getOpacity() != null) {
                        style.setOpacity(relationshipStyle.getOpacity());
                    }
                }
            }
        }

        return style;
    }

}
