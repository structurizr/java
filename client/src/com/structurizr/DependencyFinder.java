package com.structurizr;

import com.structurizr.model.Container;

import java.util.Collection;

public interface DependencyFinder {

    public void findDependencies(Container container, Collection<String> pathsToScan, Collection<String> packagesToFilter) throws Exception;

}
