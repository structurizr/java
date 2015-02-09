package com.structurizr.componentfinder;

public class NamePrefixComponentFinderStrategy extends NamingConventionComponentFinderStrategy {

    private String prefix;

    public NamePrefixComponentFinderStrategy(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected boolean matches(Class<?> type) {
        return type.getSimpleName().startsWith(prefix);
    }

}