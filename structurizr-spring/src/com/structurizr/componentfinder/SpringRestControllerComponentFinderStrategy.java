package com.structurizr.componentfinder;

/**
 * A component finder strategy for Spring REST Controllers (classes annotated @RestController).
 *
 * @author Simon Brown
 */
public final class SpringRestControllerComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringRestControllerComponentFinderStrategy() {
    }

    public SpringRestControllerComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(
            findClassesWithAnnotation(
                org.springframework.web.bind.annotation.RestController.class, SPRING_REST_CONTROLLER, includePublicTypesOnly
            )
        );
    }

}