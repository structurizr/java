package com.structurizr;

import com.structurizr.model.Container;

import java.util.Collection;

public interface ComponentFinder {

    public void findComponents(Container container, Collection<String> paths) throws Exception;

}
