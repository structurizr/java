package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy for Spring MVC Controllers (classes annotated @Controller).
 *
 * @author Simon Brown
 */
public final class SpringMvcControllerComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringMvcControllerComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        return findClassesWithAnnotation(
                org.springframework.stereotype.Controller.class,
                SPRING_MVC_CONTROLLER,
                includePublicTypesOnly
        );
    }

}