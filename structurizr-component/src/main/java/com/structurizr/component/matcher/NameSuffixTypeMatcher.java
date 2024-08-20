package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

/**
 * Matches types where the name of the type ends with the specified suffix.
 */
public class NameSuffixTypeMatcher extends AbstractTypeMatcher {

    private final String suffix;

    public NameSuffixTypeMatcher(String suffix, String technology) {
        super(technology);

        if (StringUtils.isNullOrEmpty(suffix)) {
            throw new IllegalArgumentException("A suffix must be supplied");
        }

        this.suffix = suffix;
    }

    @Override
    public boolean matches(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A type must be specified");
        }

        return type.getFullyQualifiedName().endsWith(suffix);
    }

    @Override
    public String toString() {
        return "NameSuffixTypeMatcher{" +
                "suffix='" + suffix + '\'' +
                '}';
    }

}
