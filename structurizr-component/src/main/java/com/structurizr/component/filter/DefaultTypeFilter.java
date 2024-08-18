package com.structurizr.component.filter;

import com.structurizr.component.Type;

/**
 * A type filter that accepts all types.
 */
public class DefaultTypeFilter implements TypeFilter {

    public boolean accept(Type type) {
        return true;
    }

}