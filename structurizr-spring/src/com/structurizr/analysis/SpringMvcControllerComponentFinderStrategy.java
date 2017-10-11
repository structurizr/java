package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy that finds Spring MVC controllers (classes annotated @Controller).
 */
public final class SpringMvcControllerComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringMvcControllerComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() {
        return findClassesWithAnnotation(
                org.springframework.stereotype.Controller.class,
                SPRING_MVC_CONTROLLER,
                includePublicTypesOnly
        );
    }

}