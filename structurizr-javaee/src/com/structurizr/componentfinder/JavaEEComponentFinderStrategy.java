package com.structurizr.componentfinder;

import javax.ejb.Stateless;

public class JavaEEComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    @Override
    public void findComponents() throws Exception {
        addAll(findPublicClassesWithAnnotation(Stateless.class, "Stateless session bean"));
    }

}
