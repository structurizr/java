package com.structurizr.analysis;

import java.util.regex.Pattern;

/**
 * Matches types using a regex against the fully qualified name.
 */
public class RegexTypeMatcher implements TypeMatcher {

    private Pattern regex;
    private String description;
    private String technology;

    public RegexTypeMatcher(Pattern regex, String description, String technology) {
        this.regex = regex;
        this.description = description;
        this.technology = technology;

        if (regex == null) {
            throw new IllegalArgumentException("A regex must be supplied");
        }
    }

    @Override
    public boolean matches(Class type) {
        return Pattern.matches(regex.pattern(), type.getCanonicalName());
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
