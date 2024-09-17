package com.structurizr.component.url;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;

public class PrefixUrlStrategy implements UrlStrategy {

    private final String prefix;

    public PrefixUrlStrategy(String prefix) {
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
        return prefix + type.getSource();
    }

    @Override
    public String toString() {
        return "PrefixUrlStrategy{" +
                "prefix='" + prefix + '\'' +
                '}';
    }

}