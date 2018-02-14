package com.structurizr.analysis;

/**
 * Matches types where the name of the type ends with the specified suffix.
 */
public class NameSuffixTypeMatcher extends AbstractTypeMatcher {

    private String suffix;

    public NameSuffixTypeMatcher(String suffix, String description, String technology) {
        super(description, technology);

        if (suffix == null || suffix.trim().length() == 0) {
            throw new IllegalArgumentException("A suffix must be supplied");
        }

        this.suffix = suffix;
    }

    @Override
    public boolean matches(Class type) {
        return type.getSimpleName().endsWith(suffix);
    }

}
