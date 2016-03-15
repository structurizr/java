package com.structurizr.componentfinder;

public interface TypeMatcher {

    boolean matches(Class type);

    String getDescription();

    String getTechnology();

}
