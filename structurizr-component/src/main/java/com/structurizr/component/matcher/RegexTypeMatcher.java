package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Matches types using a regex against the fully qualified type name.
 */
public class RegexTypeMatcher implements TypeMatcher {

    private final Pattern regex;

    public RegexTypeMatcher(String regex) {
        if (StringUtils.isNullOrEmpty(regex)) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = Pattern.compile(regex);
    }

    @Override
    public boolean matches(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A type must be specified");
        }

        return Pattern.matches(regex.pattern(), type.getFullyQualifiedName());
    }

    @Override
    public String toString() {
        return "RegexTypeMatcher{" +
                "regex='" + regex + "'" +
                '}';
    }

}
