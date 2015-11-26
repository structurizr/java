package com.structurizr.componentfinder;

/**
 * Finds components based upon the start of the class name. For example *Controller.
 */
public class NamePrefixComponentFinderStrategy extends AbstractNamingConventionComponentFinderStrategy {

    private String prefix;

    public NamePrefixComponentFinderStrategy(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected boolean matches(Class<?> type) {
        return type.getSimpleName().startsWith(prefix);
    }

}