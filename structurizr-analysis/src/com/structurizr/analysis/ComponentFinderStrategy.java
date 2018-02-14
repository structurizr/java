package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * The interface that all component finder strategies must implement.
 */
public interface ComponentFinderStrategy {

    /**
     * Sets a reference to the parent component finder.
     *
     * @param componentFinder   a ComponentFinder instance
     */
    void setComponentFinder(ComponentFinder componentFinder);

    /**
     * Called before all component finder strategies belonging to the
     * same component finder are asked to find components.
     *
     * @throws Exception    if something goes wrong
     */
    void beforeFindComponents() throws Exception;

    /**
     * Finds components.
     *
     * @return the set of components found
     * @throws Exception    if something goes wrong
     */
    Set<Component> findComponents() throws Exception;

    /**
     * Called after all component finder strategies belonging to the
     * same component finder have found components. This can be used
     * to supplement the component with more information, such as
     * dependencies.
     *
     * @throws Exception    if something goes wrong
     */
    void afterFindComponents() throws Exception;

}