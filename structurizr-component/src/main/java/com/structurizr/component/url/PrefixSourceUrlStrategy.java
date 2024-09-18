package com.structurizr.component.url;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

/**
 * Adds a given prefix to the component source location.
 */
public class PrefixSourceUrlStrategy implements UrlStrategy {

    private final String prefix;

    public PrefixSourceUrlStrategy(String prefix) {
        if (StringUtils.isNullOrEmpty(prefix)) {
            throw new IllegalArgumentException("A prefix must be supplied");
        }

        if (!prefix.endsWith("/")) {
            prefix = prefix + "/";
        }

        this.prefix = prefix;
    }

    @Override
    public String urlOf(Type type) {
        return prefix + (type.getSource().replaceAll("\\\\", "/"));
    }

    @Override
    public String toString() {
        return "PrefixUrlStrategy{" +
                "prefix='" + prefix + '\'' +
                '}';
    }

}