package com.structurizr.component.description;

import com.structurizr.component.Type;

/**
 * Provides a way customise how component descriptions are generated.
 */
public interface DescriptionStrategy {

    String descriptionOf(Type type);

}