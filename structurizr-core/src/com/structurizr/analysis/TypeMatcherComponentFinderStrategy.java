package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.*;

/**
 * A component finder strategy that uses type information to find components, based upon a number
 * of pluggable {@link TypeMatcher} implementations.
 */
public class TypeMatcherComponentFinderStrategy extends AbstractComponentFinderStrategy {

    private List<TypeMatcher> typeMatchers = new LinkedList<>();

    public TypeMatcherComponentFinderStrategy(TypeMatcher... typeMatchers) {
        this.typeMatchers.addAll(Arrays.asList(typeMatchers));
    }

    @Override
    protected Set<Component> doFindComponents() {
        Set<Component> components = new HashSet<>();

        Set<Class<?>> types = getTypeRepository().getAllTypes();
        for (Class type : types) {
            for (TypeMatcher typeMatcher : typeMatchers) {
                if (typeMatcher.matches(type)) {
                    Component component = getComponentFinder().getContainer().addComponentAndCode(
                            type,
                            typeMatcher.getDescription(),
                            typeMatcher.getTechnology());
                    components.add(component);
                }
            }
        }

        return components;
    }

}