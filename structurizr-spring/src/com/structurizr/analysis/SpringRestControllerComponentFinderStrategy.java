package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy that finds Spring REST controllers (classes annotated @RestController).
 */
public final class SpringRestControllerComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringRestControllerComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        return findClassesWithAnnotation(
                org.springframework.web.bind.annotation.RestController.class,
                SPRING_REST_CONTROLLER,
                includePublicTypesOnly
        );
    }

}