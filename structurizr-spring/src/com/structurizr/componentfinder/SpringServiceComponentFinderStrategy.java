package com.structurizr.componentfinder;

/**
 * A component finder strategy for Spring Services (classes annotated @Service).
 *
 * @author Simon Brown
 */
public final class SpringServiceComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringServiceComponentFinderStrategy() {
    }

    public SpringServiceComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(
            findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Service.class, SPRING_SERVICE
            )
        );
    }

}