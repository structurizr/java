package com.structurizr.componentfinder;

public interface ComponentFinderStrategy {

    public void findComponents() throws Exception;

    public void findDependencies() throws Exception;

    public void setComponentFinder(ComponentFinder componentFinder);

}
