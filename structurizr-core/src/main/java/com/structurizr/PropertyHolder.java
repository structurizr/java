package com.structurizr;

import java.util.Map;

public interface PropertyHolder {

    /**
     * Gets the collection of name-value property pairs associated with this object, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties();

    /**
     * Adds a name-value pair property to this object.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value);

}
