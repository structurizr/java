package com.structurizr.component.matcher;

import com.structurizr.component.Type;

import java.util.regex.Pattern;

/**
 * Matches types using a regex against the fully qualified type name.
 */
public class RegexTypeMatcher extends AbstractTypeMatcher {

    private final Pattern regex;

    public RegexTypeMatcher(String regex, String technology) {
        super(technology);

        if (regex == null) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = Pattern.compile(regex);
    }

    public RegexTypeMatcher(Pattern regex, String technology) {
        super(technology);

        if (regex == null) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = regex;
    }

    @Override
    public boolean matches(Type type) {
        if (type != null && type.getFullyQualifiedName() != null) {
            return Pattern.matches(regex.pattern(), type.getFullyQualifiedName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "RegexTypeMatcher{" +
                "regex=" + regex +
                '}';
    }

}
