package com.structurizr.component.description;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

/**
 * Truncates the type description to the max length, appending "..." when truncated.
 */
public class TruncatedDescriptionStrategy implements DescriptionStrategy {

    private final int maxLength;

    public TruncatedDescriptionStrategy(int maxLength) {
        if (maxLength < 1) {
            throw new IllegalArgumentException("Max length must be greater than 0");
        }

        this.maxLength = maxLength;
    }

    @Override
    public String descriptionOf(Type type) {
        String description = type.getDescription();

        if (StringUtils.isNullOrEmpty(description)) {
            return description;
        }

        if (description.length() > maxLength) {
            return description.substring(0, maxLength) + "...";
        } else {
            return description;
        }
    }

    @Override
    public String toString() {
        return "TruncatedDescriptionStrategy{" +
                "maxLength=" + maxLength +
                '}';
    }
}