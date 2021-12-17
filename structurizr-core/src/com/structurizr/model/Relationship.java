package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.Url;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A relationship between two elements.
 */
public final class Relationship extends ModelItem {

    private Model model;

    private Element source;
    private String sourceId;
    private Element destination;
    private String destinationId;
    private String description;
    private String technology;
    private InteractionStyle interactionStyle;

    private String linkedRelationshipId;

    Relationship() {
    }

    Relationship(Element source, Element destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        this();

        setSource(source);
        setDestination(destination);
        setDescription(description);
        setTechnology(technology);
        setInteractionStyle(interactionStyle);

        addTags(tags);
    }

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    @JsonIgnore
    public Element getSource() {
        return source;
    }

    /**
     * Gets the ID of the source element.
     *
     * @return  the ID of the source element, as a String
     */
    public String getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    void setSource(Element source) {
        this.source = source;
    }

    @JsonIgnore
    public Element getDestination() {
        return destination;
    }

    /**
     * Gets the ID of the destination element.
     *
     * @return  the ID of the destination element, as a String
     */
    public String getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    void setDestination(Element destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the technology associated with this relationship (e.g. HTTPS, JDBC, etc).
     *
     * @return  the technology as a String,
     *          or null if a technology is not specified
     */
    public String getTechnology() {
        return technology;
    }

    void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the interaction style (synchronous or asynchronous).
     *
     * @return  an InteractionStyle,
     *          or null if an interaction style has not been specified
     */
    public InteractionStyle getInteractionStyle() {
        return interactionStyle;
    }

    void setInteractionStyle(InteractionStyle interactionStyle) {
        this.interactionStyle = interactionStyle;
    }

    public String getLinkedRelationshipId() {
        return linkedRelationshipId;
    }

    void setLinkedRelationshipId(String baseRelationshipId) {
        this.linkedRelationshipId = baseRelationshipId;
    }

    @Override
    public Set<String> getDefaultTags() {
        if (linkedRelationshipId == null) {
            Set<String> tags = new LinkedHashSet<>();
            tags.add(Tags.RELATIONSHIP);

            if (interactionStyle == InteractionStyle.Synchronous) {
                tags.add(Tags.SYNCHRONOUS);
            } else if (interactionStyle == InteractionStyle.Asynchronous) {
                tags.add(Tags.ASYNCHRONOUS);
            }

            return tags;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public String toString() {
        return source.toString() + " ---[" + description + "]---> " + destination.toString();
    }

}