package com.structurizr.componentfinder;

public class NameSuffixComponentFinderStrategy extends NamingConventionComponentFinderStrategy {

    private String suffix;

    public NameSuffixComponentFinderStrategy(String suffix) {
        this.suffix = suffix;
    }

    @Override
    protected boolean matches(Class<?> type) {
        return type.getSimpleName().endsWith(suffix);
    }

}