package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * A component finder strategy that finds Spring web service endpoints (classes annotated @Endpoint).
 */
public final class SpringWebserviceEndpointComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringWebserviceEndpointComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() {
        return findClassesWithAnnotation(
                org.springframework.ws.server.endpoint.annotation.Endpoint.class,
                SPRING_WEB_SERVICE_ENDPOINT,
                includePublicTypesOnly
        );
    }

}