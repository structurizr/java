package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * The base class for elements and relationships.
 */
abstract class ModelItem {

    private String id = "";
    private Set<String> tags = new LinkedHashSet<>();
    private Map<String, String> properties = new HashMap<>();

    protected abstract Set<String> getRequiredTags();

    /**
     * Gets the ID of this item in the model.
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
     * Gets the comma separated list of tags.
     *
     * @return  a comma separated list of tags,
     *          or an empty string if there are no tags
     */
    public String getTags() {
        Set<String> setOfTags = getTagsAsSet();

        if (setOfTags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : setOfTags) {
            buf.append(tag);
            buf.append(",");
        }

        String tagsAsString = buf.toString();
        return tagsAsString.substring(0, tagsAsString.length()-1);
    }

    @JsonIgnore
    public Set<String> getTagsAsSet() {
        Set<String> setOfTags = new LinkedHashSet<>(getRequiredTags());
        setOfTags.addAll(tags);

        return setOfTags;
    }

    void setTags(String tags) {
        this.tags.clear();

        if (tags == null) {
            return;
        }

        Collections.addAll(this.tags, tags.split(","));
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag);
            }
        }
    }

    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag);
        }
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
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
            this.properties = new HashMap<>(properties);
        }
    }

}