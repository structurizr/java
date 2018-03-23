package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.Url;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
public abstract class Element extends Taggable {

    static final String CANONICAL_NAME_SEPARATOR = "/";

    private Model model;

    private String id = "";
    private String name;
    private String description;
    private String url;
    private Map<String, String> properties = new HashMap<>();

    private Set<Relationship> relationships = new LinkedHashSet<>();

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
    void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The name of an element must not be null or empty.");
        }

        this.name = name;
    }

    /**
     * Gets the URL where more information about this element can be found.
     *
     * @return  a URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this element can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the collection of name-value property pairs associated with this element, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property to this element.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    @JsonIgnore
    public abstract String getCanonicalName();

    String formatForCanonicalName(String name) {
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
    
    void removeRelationship(Relationship relationship) {
        relationships.remove(relationship);
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    @Override
    public int hashCode() {
        if (name != null) {
            return name.hashCode();
        } else {
            return super.hashCode();
        }
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