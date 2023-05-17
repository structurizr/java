package com.structurizr;

import javax.annotation.Nonnull;
import java.util.Map;

public interface PropertyHolder {

    /**
     * Gets the collection of name-value property pairs associated with this workspace, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    @Nonnull
    Map<String, String> getProperties();

    /**
     * Adds a name-value pair property to this workspace.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    void addProperty(@Nonnull String name, @Nonnull String value);

}
