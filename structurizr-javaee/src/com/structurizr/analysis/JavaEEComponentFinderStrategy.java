package com.structurizr.analysis;

import com.structurizr.model.Component;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.Set;

public class JavaEEComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public JavaEEComponentFinderStrategy() {
        super(new FirstImplementationOfInterfaceSupportingTypesStrategy());
    }

    public JavaEEComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() {
        Set<Component> components = new HashSet<>();

        components.addAll(findClassesWithAnnotation(Path.class, "JAX-RS web service"));
        components.addAll(findClassesWithAnnotation(ServerEndpoint.class, "Websocket endpoint"));
        components.addAll(findClassesWithAnnotation(Stateless.class, "Stateless session bean"));
        components.addAll(findClassesWithAnnotation(Stateful.class, "Stateful session bean"));
        components.addAll(findClassesWithAnnotation(Singleton.class, "Singleton session bean"));
        components.addAll(findClassesWithAnnotation(Named.class, "Named bean"));

        return components;
    }

}
