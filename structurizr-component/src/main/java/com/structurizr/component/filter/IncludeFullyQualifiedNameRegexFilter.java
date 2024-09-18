package com.structurizr.component.filter;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

/**
 * A type filter that includes by matching a regex against the fully qualified type name.
 */
public class IncludeFullyQualifiedNameRegexFilter implements TypeFilter {

    private final String regex;

    public IncludeFullyQualifiedNameRegexFilter(String regex) {
        if (StringUtils.isNullOrEmpty(regex)) {
            throw new IllegalArgumentException("A regex must be supplied");
        }

        this.regex = regex;
    }

    @Override
    public boolean accept(Type type) {
        return type.getFullyQualifiedName().matches(regex);
    }

    @Override
    public String toString() {
        return "IncludeFullyQualifiedNameRegexFilter{" +
                "regex='" + regex + '\'' +
                '}';
    }

}