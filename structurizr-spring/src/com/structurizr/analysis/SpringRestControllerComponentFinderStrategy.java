package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy for Spring REST Controllers (classes annotated @RestController).
 *
 * @author Simon Brown
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