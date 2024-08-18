package com.structurizr.component.filter;

import com.structurizr.component.Type;

/**
 * A type filter that excludes abstract types.
 */
public class ExcludeAbstractClassTypeFilter implements TypeFilter {

    public boolean accept(Type type) {
        return !type.isAbstract();
    }

}