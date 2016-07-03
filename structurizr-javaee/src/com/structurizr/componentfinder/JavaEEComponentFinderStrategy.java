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
        addAll(findPublicClassesWithAnnotation(Path.class, "JAX-RS web service"));
        addAll(findPublicClassesWithAnnotation(ServerEndpoint.class, "Websocket endpoint"));
        addAll(findPublicClassesWithAnnotation(Stateless.class, "Stateless session bean"));
        addAll(findPublicClassesWithAnnotation(Stateful.class, "Stateful session bean"));
        addAll(findPublicClassesWithAnnotation(Singleton.class, "Singleton session bean"));
        addAll(findPublicClassesWithAnnotation(Named.class, "Named bean"));
    }

}
