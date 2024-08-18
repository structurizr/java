package com.structurizr.component.matcher;

import com.structurizr.component.Type;

/**
 * Matches types where the name of the type ends with the specified suffix.
 */
public class NameSuffixTypeMatcher extends AbstractTypeMatcher {

    private final String suffix;

    public NameSuffixTypeMatcher(String suffix, String technology) {
        super(technology);

        if (suffix == null || suffix.trim().length() == 0) {
            throw new IllegalArgumentException("A suffix must be supplied");
        }

        this.suffix = suffix;
    }

    @Override
    public boolean matches(Type type) {
        return type.getFullyQualifiedName().endsWith(suffix);
    }

}
