package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.Collection;

public interface ComponentFinderStrategy {

    public Collection<Component> findComponents() throws Exception;

    public void findDependencies() throws Exception;

    public void setComponentFinder(ComponentFinder componentFinder);

}
