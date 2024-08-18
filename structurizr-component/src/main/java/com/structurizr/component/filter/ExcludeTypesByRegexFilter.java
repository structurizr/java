package com.structurizr.component.filter;

import com.structurizr.component.Type;

/**
 * A type filter that excludes by matching a regex against the fully qualified type name.
 */
public class ExcludeTypesByRegexFilter implements TypeFilter {

    private final String[] regexes;

    public ExcludeTypesByRegexFilter(String... regexes) {
        this.regexes = regexes;
    }

    @Override
    public boolean accept(Type type) {
        for (String regex : regexes) {
            if (type.getFullyQualifiedName().matches(regex)) {
                return false;
            }
        }

        return true;
    }

}