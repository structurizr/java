package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is the superclass for all model elements.
 */
public abstract class Element extends ModelItem {

    private Model model;

    private String name;
    private String description;

    private Set<Relationship> relationships = new TreeSet<>();

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
    void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The name of an element must not be null or empty.");
        }

        this.name = name;
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
        return new TreeSet<>(relationships);
    }

    void setRelationships(Set<Relationship> relationships) {
        if (relationships != null) {
            this.relationships = new TreeSet<>(relationships);
        }
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
     * Determines whether this element has an efferent (outgoing) relationship with
     * the specified element and description.
     *
     * @param element       the element to look for
     * @param description   the relationship description
     * @return  true if this element has an efferent relationship with the specified element and description,
     *          false otherwise
     */
    public boolean hasEfferentRelationshipWith(Element element, String description) {
        return getEfferentRelationshipWith(element, description) != null;
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

    /**
     * Gets the efferent (outgoing) relationship with the specified element.
     *
     * @param element   the element to look for
     * @return  a Set of Relationship objects; empty if no relationships exist
     */
    public Set<Relationship> getEfferentRelationshipsWith(Element element) {
        Set<Relationship> set = new TreeSet<>();

        if (element != null) {
            for (Relationship relationship : relationships) {
                if (relationship.getDestination().equals(element)) {
                    set.add(relationship);
                }
            }
        }

        return set;
    }

    /**
     * Gets the efferent (outgoing) relationship with the specified element and description.
     *
     * @param element       the element to look for
     * @param description   the relationship description
     * @return  a Relationship object, or null if the specified relationship doesn't exist
     */
    public Relationship getEfferentRelationshipWith(Element element, String description) {
        if (element == null) {
            return null;
        }

        if (description == null) {
            description = "";
        }

        for (Relationship relationship : relationships) {
            if (relationship.getDestination().equals(element) && description.equals(relationship.getDescription())) {
                return relationship;
            }
        }

        return null;
    }

    boolean has(Relationship relationship) {
        return relationships.stream().anyMatch(r -> r.getDestination().equals(relationship.getDestination()) && r.getDescription().equals(relationship.getDescription()));
    }

    void add(Relationship relationship) {
        relationships.add(relationship);
    }

    void remove(Relationship relationship) {
        relationships.remove(relationship);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and the specified custom element.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull CustomElement destination, String description) {
        return uses(destination, description, null);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and the specified custom element.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull CustomElement destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology, null);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and the specified custom element.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @param tags             an array of tags
     * @return the relationship that has just been created and added to the model
     */
    @Nullable
    public Relationship uses(@Nonnull CustomElement destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle, tags);
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

}