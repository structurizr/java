package com.structurizr.model;

import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a custom element.
 */
public final class CustomElement extends GroupableElement {

    private String metadata;

    protected CustomElement() {
    }

    @Override
    public Element getParent() {
        return null;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Collections.singletonList(Tags.ELEMENT));
    }

    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    /**
     * Adds a unidirectional "uses" style relationship between this custom element and the specified element.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull Element destination, String description) {
        return uses(destination, description, null);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this custom element and the specified element.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull Element destination, String description, String technology) {
        return uses(destination, description, technology, null);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this custom element and the specified element.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle) {
        return uses(destination, description, technology, interactionStyle, null);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this custom element and the specified element.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @param tags             an array of tags
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle, tags);
    }

}