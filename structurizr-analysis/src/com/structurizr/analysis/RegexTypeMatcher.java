package com.structurizr.analysis;

import java.util.regex.Pattern;

/**
 * Matches types using a regex against the fully qualified type name.
 */
public class RegexTypeMatcher extends AbstractTypeMatcher {

    private Pattern regex;

    public RegexTypeMatcher(String regex, String description, String technology) {
        super(description, technology);

        if (regex == null) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = Pattern.compile(regex);
    }

    public RegexTypeMatcher(Pattern regex, String description, String technology) {
        super(description, technology);

        if (regex == null) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = regex;
    }

    @Override
    public boolean matches(Class type) {
        if (type != null && type.getCanonicalName() != null) {
            return Pattern.matches(regex.pattern(), type.getCanonicalName());
        } else {
            return false;
        }
    }

}
