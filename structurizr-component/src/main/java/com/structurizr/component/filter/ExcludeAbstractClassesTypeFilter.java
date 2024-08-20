package com.structurizr.component.filter;

import com.structurizr.component.Type;

/**
 * A type filter that excludes abstract types.
 */
public class ExcludeAbstractClassesTypeFilter implements TypeFilter {

    public boolean accept(Type type) {
        return !type.isAbstractClass();
    }

    @Override
    public String toString() {
        return "ExcludeAbstractClassTypeFilter{}";
    }

}