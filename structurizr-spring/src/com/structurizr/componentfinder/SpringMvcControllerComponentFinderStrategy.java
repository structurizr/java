package com.structurizr.componentfinder;

/**
 * A component finder strategy for Spring MVC Controllers (classes annotated @Controller).
 *
 * @author Simon Brown
 */
public final class SpringMvcControllerComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringMvcControllerComponentFinderStrategy() {
    }

    public SpringMvcControllerComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(
            findClassesWithAnnotation(
                org.springframework.stereotype.Controller.class, SPRING_MVC_CONTROLLER, includePublicTypesOnly
            )
        );
    }

}