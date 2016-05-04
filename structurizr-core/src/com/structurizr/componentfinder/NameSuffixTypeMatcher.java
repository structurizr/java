package com.structurizr.componentfinder;

/**
 * Matches types where the name of the type ends with the specified suffix.
 */
public class NameSuffixTypeMatcher implements TypeMatcher {

    private String suffix;
    private String description;
    private String technology;

    public NameSuffixTypeMatcher(String suffix, String description, String technology) {
        this.suffix = suffix;
        this.description = description;
        this.technology = technology;

        if (suffix == null || suffix.trim().length() == 0) {
            throw new IllegalArgumentException("A suffix must be supplied");
        }
    }

    @Override
    public boolean matches(Class type) {
        return type.getSimpleName().endsWith(suffix);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTechnology() {
        return technology;
    }

}
