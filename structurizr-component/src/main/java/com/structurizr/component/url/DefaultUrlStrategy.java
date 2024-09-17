package com.structurizr.component.url;

import com.structurizr.component.Type;

/**
 * Generates a null URL.
 */
public class DefaultUrlStrategy implements UrlStrategy {

    @Override
    public String urlOf(Type type) {
        return null;
    }

    @Override
    public String toString() {
        return "DefaultUrlStrategy{}";
    }

}