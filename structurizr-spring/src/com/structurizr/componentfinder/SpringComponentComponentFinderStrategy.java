package com.structurizr.componentfinder;

/**
 * A component finder strategy for Spring Components (classes annotated @Component).
 *
 * @author Simon Brown
 */
public final class SpringComponentComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringComponentComponentFinderStrategy() {
    }

    public SpringComponentComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(
            findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Component.class, SPRING_COMPONENT
            )
        );
    }

}