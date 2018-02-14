package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy that finds Spring Services (classes annotated @Service).
 */
public final class SpringServiceComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringServiceComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Service.class,
                SPRING_SERVICE
        );
    }

}