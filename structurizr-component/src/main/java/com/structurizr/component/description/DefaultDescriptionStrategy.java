package com.structurizr.component.description;

import com.structurizr.component.Type;
import com.structurizr.component.naming.NamingStrategy;

/**
 * Uses the type description as-is.
 */
public class DefaultDescriptionStrategy implements DescriptionStrategy {

    @Override
    public String descriptionOf(Type type) {
        return type.getDescription();
    }

    @Override
    public String toString() {
        return "DefaultDescriptionStrategy{}";
    }

}