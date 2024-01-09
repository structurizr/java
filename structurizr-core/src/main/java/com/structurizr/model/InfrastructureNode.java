package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * <p>
 *   Represents an infrastructure node, which is something like:
 * </p>
 *
 * <ul>
 *     <li>Load balancer</li>
 *     <li>Firewall</li>
 *     <li>DNS service</li>
 *     <li>etc</li>
 * </ul>
 */
public final class InfrastructureNode extends DeploymentElement {

    private DeploymentNode parent;
    private String technology;

    /**
     * Adds a relationship between this and another deployment element (deployment node, infrastructure node, or container instance).
     *
     * @param destination   the destination DeploymentElement
     * @param description   a short description of the relationship
     * @param technology    the technology
     * @return              a Relationship object
     */
    public Relationship uses(DeploymentElement destination, String description, String technology) {
        return uses(destination, description, technology, InteractionStyle.Synchronous);
    }

    /**
     * Adds a relationship between this and another deployment element (deployment node, infrastructure node, or container instance).
     *
     * @param destination       the destination DeploymentElement
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentElement destination, String description, String technology, InteractionStyle interactionStyle) {
        return uses(destination, description, technology, interactionStyle, new String[0]);
    }

    /**
     * Adds a relationship between this and another deployment element (deployment node, infrastructure node, or container instance).
     *
     * @param destination       the destination DeploymentElement
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @param tags              an array of tags
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentElement destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle, tags);
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    @JsonIgnore
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.INFRASTRUCTURE_NODE));
    }

    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

}