package com.structurizr.view;

import com.structurizr.PropertyHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStyle implements PropertyHolder {

    @Nonnull
    private Map<String, String> properties = new HashMap<>();

    /**
     * Gets the collection of name-value property pairs associated with this workspace, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    @Nonnull
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property to this workspace.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(@Nonnull String name, @Nonnull String value) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(@Nullable Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

}
