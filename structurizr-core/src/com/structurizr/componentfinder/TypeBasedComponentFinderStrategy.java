package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.*;

/**
 * A component finder strategy that uses type information to find components, based upon a number
 * of pluggable {@link TypeMatcher} implementations.
 */
public class TypeBasedComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    private List<TypeMatcher> typeMatchers = new LinkedList<>();

    public TypeBasedComponentFinderStrategy(TypeMatcher... typeMatchers) {
        this.typeMatchers.addAll(Arrays.asList(typeMatchers));
    }

    @Override
    public void findComponents() throws Exception {
        Set<String> typeNames = getAllTypeNames();
        for (String typeName : typeNames) {
            Class type = Class.forName(typeName);
            for (TypeMatcher typeMatcher : typeMatchers) {
                if (typeMatcher.matches(type)) {
                    Component component = getComponentFinder().getContainer().addComponent(
                            type.getSimpleName(),
                            type.getCanonicalName(),
                            typeMatcher.getDescription(),
                            typeMatcher.getTechnology());
                    add(component);
                }
            }
        }
    }

}