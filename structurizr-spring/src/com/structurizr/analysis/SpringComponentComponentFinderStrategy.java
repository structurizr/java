package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy for Spring Components (classes annotated @Component).
 *
 * @author Simon Brown
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