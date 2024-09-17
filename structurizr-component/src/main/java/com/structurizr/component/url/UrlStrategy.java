package com.structurizr.component.url;

import com.structurizr.component.Type;

/**
 * Provides a way customise how component URLs are generated.
 */
public interface UrlStrategy {

    String urlOf(Type type);

}