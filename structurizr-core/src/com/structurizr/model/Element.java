package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
public abstract class Element extends Taggable {

    public static final String CANONICAL_NAME_SEPARATOR = "/";

    private Model model;

    protected String id = "";

    protected String name;
    protected String description;

    protected Set<Relationship> relationships = new LinkedHashSet<>();

    protected Element() {
    }

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    /**
     * Gets the ID of this element in the model.
     *
     * @return  the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of this person.
     *
     * @return  the name, as a String
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a description of this person.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract Element getParent();

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

    @JsonIgnore
    public abstract String getCanonicalName();

    protected String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and software system.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     */
    public Relationship uses(SoftwareSystem destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology    the technology details (e.g. JSON/HTTPS)
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination       the target of the relationship
     * @param description       a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology        the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle  the interaction style (sync vs async)
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and container.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     */
    public Relationship uses(Container destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology    the technology details (e.g. JSON/HTTPS)
     */
    public Relationship uses(Container destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination       the target of the relationship
     * @param description       a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology        the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle  the interaction style (sync vs async)
     */
    public Relationship uses(Container destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and component.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     */
    public Relationship uses(Component destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology    the technology details (e.g. JSON/HTTPS)
     */
    public Relationship uses(Component destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination       the target of the relationship
     * @param description       a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology        the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle  the interaction style (sync vs async)
     */
    public Relationship uses(Component destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "sends e-mail to")
     */
    public Relationship delivers(Person destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "sends e-mail to")
     * @param technology    the technology details (e.g. JSON/HTTPS)
     */
    public Relationship delivers(Person destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination       the target of the relationship
     * @param description       a description of the relationship (e.g. "sends e-mail to")
     * @param technology        the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle  the interaction style (sync vs async)
     */
    public Relationship delivers(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    public boolean hasAfferentRelationships() {
        return getModel().getRelationships().stream().filter(r -> r.getDestination() == this).count() > 0;
    }

    public boolean hasEfferentRelationshipWith(Element element) {
        return getEfferentRelationshipWith(element) != null;
    }

    public Relationship getEfferentRelationshipWith(Element element) {
        if (element == null) {
            return null;
        }

        for (Relationship relationship : relationships) {
            if (relationship.getDestination().equals(element)) {
                return relationship;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Element)) {
            return false;
        }

        Element element = (Element)o;
        return getCanonicalName().equals(element.getCanonicalName());
    }

}
