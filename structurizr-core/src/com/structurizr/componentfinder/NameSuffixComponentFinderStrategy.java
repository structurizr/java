package com.structurizr.componentfinder;

/**
 * Finds components based upon the end of the class name. For example *Controller.
 */
public class NameSuffixComponentFinderStrategy extends AbstractNamingConventionComponentFinderStrategy {

    private String suffix;

    public NameSuffixComponentFinderStrategy(String suffix) {
        this.suffix = suffix;
    }

    @Override
    protected boolean matches(Class<?> type) {
        return type.getSimpleName().endsWith(suffix);
    }

}