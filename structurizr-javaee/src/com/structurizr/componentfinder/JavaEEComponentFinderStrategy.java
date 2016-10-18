package com.structurizr.componentfinder;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Path;

public class JavaEEComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public JavaEEComponentFinderStrategy() {
        super(new FirstImplementationOfInterfaceSupportingTypesStrategy());
    }

    public JavaEEComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(findClassesWithAnnotation(Path.class, "JAX-RS web service"));
        addAll(findClassesWithAnnotation(ServerEndpoint.class, "Websocket endpoint"));
        addAll(findClassesWithAnnotation(Stateless.class, "Stateless session bean"));
        addAll(findClassesWithAnnotation(Stateful.class, "Stateful session bean"));
        addAll(findClassesWithAnnotation(Singleton.class, "Singleton session bean"));
        addAll(findClassesWithAnnotation(Named.class, "Named bean"));
    }

}
