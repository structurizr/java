package com.structurizr.dsl;

import com.structurizr.PropertyHolder;
import com.structurizr.model.Perspective;
import com.structurizr.util.StringUtils;

import java.util.*;

final class Archetype implements PropertyHolder {

    private final String name;
    private final String type;
    private String description = "";
    private String technology = "";
    private final Set<String> tags = new LinkedHashSet<>();

    private Map<String, String> properties = new HashMap<>();
    private final Set<Perspective> perspectives = new TreeSet<>();

    Archetype(String name, String type) {
        if (StringUtils.isNullOrEmpty(name)) {
            name = type;
        }

        this.name = name.toLowerCase();
        this.type = type;
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getTechnology() {
        return technology;
    }

    void setTechnology(String technology) {
        this.technology = technology;
    }

    void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag.trim());
            }
        }
    }

    Set<String> getTags() {
        return new LinkedHashSet<>(tags);
    }

    /**
     * Gets the collection of name-value property pairs associated with this model item, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Adds a name-value pair property to this model item.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (StringUtils.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

}