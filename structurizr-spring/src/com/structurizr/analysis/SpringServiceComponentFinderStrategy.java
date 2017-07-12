package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy for Spring Services (classes annotated @Service).
 *
 * @author Simon Brown
 */
public final class SpringServiceComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringServiceComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Service.class,
                SPRING_SERVICE
        );
    }

}