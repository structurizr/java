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

        if (StringUtils.isNullOrEmpty(description)) {
            return "";
        }

        int index = description.indexOf('.');
        if (index == -1) {
            return description.trim();
        } else {
            return description.trim().substring(0, index+1);
        }
    }

    @Override
    public String toString() {
        return "FirstSentenceDescriptionStrategy{}";
    }

}