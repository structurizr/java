package com.structurizr.component.description;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

/**
 * Uses the first sentence of the type description, or the description as-is if there are no sentences.
 */
public class FirstSentenceDescriptionStrategy implements DescriptionStrategy {

    @Override
    public String descriptionOf(Type type) {
        String description = type.getDescription();

        int index = description.indexOf('.');
        if (index == -1) {
            return description;
        } else {
            return description.substring(0, index+1);
        }
    }

    @Override
    public String toString() {
        return "FirstSentenceDescriptionStrategy{}";
    }

}