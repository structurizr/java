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
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of this element.
     *
     * @return the name, as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this element.
     *
     * @param name  the name, as a String
     */
    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public abstract String getCanonicalName();

    protected String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }

    /**
     * Gets a description of this element.
     *
     * @return the description, as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this element.
     *
     * @param description   the description, as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the parent of this element.
     *
     * @return  the parent Element, or null if this element doesn't have a parent (i.e. a Person or SoftwareSystem)
     */
    public abstract Element getParent();

    /**
     * Gets the set of outgoing relationships.
     *
     * @return  a Set of Relationship objects, or an empty set if none exist
     */
    public Set<Relationship> getRelationships() {
        return new LinkedHashSet<>(relationships);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and software system.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and container.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and component.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "sends e-mail to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Person destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "sends e-mail to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Person destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "sends e-mail to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Determines whether this element has afferent (incoming) relationships.
     *
     * @return  true if this element has afferent relationships, false otherwise
     */
    public boolean hasAfferentRelationships() {
        return getModel().getRelationships().stream().filter(r -> r.getDestination() == this).count() > 0;
    }

    /**
     * Determines whether this element has an efferent (outgoing) relationship with
     * the specified element.
     *
     * @param element   the element to look for
     * @return  true if this element has an efferent relationship with the specified element,
     *          false otherwise
     */
    public boolean hasEfferentRelationshipWith(Element element) {
        return getEfferentRelationshipWith(element) != null;
    }

    /**
     * Gets the efferent (outgoing) relationship with the specified element.
     *
     * @param element   the element to look for
     * @return  a Relationship object if an efferent relationship exists, null otherwise
     */
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

    boolean has(Relationship relationship) {
        return relationships.contains(relationship);
    }

    void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Element)) {
            return false;
        }

        if (!this.getClass().equals(o.getClass())) {
            return false;
        }

        Element element = (Element)o;
        return getCanonicalName().equals(element.getCanonicalName());
    }

}
