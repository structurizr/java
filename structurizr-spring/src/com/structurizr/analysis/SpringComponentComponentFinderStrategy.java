package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy that finds Spring components (classes annotated @Component).
 */
public final class SpringComponentComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringComponentComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Component.class,
                SPRING_COMPONENT
        );
    }

}