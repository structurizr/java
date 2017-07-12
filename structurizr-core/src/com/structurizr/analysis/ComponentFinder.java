package com.structurizr.analysis;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.*;
import java.util.regex.Pattern;

/**
 * This class allows you to find components in a Java codebase, when used in conjunction
 * with a number of pluggable component finder strategies.
 */
public class ComponentFinder {

    private Container container;
    private String packageName;

    // this is a default of regexes representing types we're probably not interested in */
    private Set<Pattern> exclusions = new HashSet<>(Arrays.asList(
            Pattern.compile("java\\..*"),
            Pattern.compile("javax\\..*"),
            Pattern.compile("sun\\..*")
    ));

    // the list of strategies, which will be executed in the order they are added
    private List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    /**
     * Create a new component finder.
     *
     * @param container                     the Container that components will be added to
     * @param packageName                 the Java package name to be scanned (e.g. "com.mycompany.myapp")
     * @param componentFinderStrategies     one or more ComponentFinderStrategy objects, describing how to find components
     */
    public ComponentFinder(Container container, String packageName, ComponentFinderStrategy... componentFinderStrategies) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        if (packageName == null || packageName.trim().length() == 0) {
            throw new IllegalArgumentException("A package name must be specified.");
        }

        if (componentFinderStrategies.length == 0) {
            throw new IllegalArgumentException("One or more ComponentFinderStrategy objects must be specified.");
        }

        this.container = container;
        this.packageName = packageName;

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            this.componentFinderStrategies.add(componentFinderStrategy);
            componentFinderStrategy.setComponentFinder(this);
        }
    }

    /**
     * Find components, using all of the configured component finder strategies
     * in the order they were added.
     *
     * @return  the set of Components that were found
     * @throws Exception    if something goes wrong
     */
    public Set<Component> findComponents() throws Exception {
        Set<Component> componentsFound = new HashSet<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentsFound.addAll(componentFinderStrategy.findComponents());
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.postFindComponents();
        }

        return componentsFound;
    }

    /**
     * Gets the Container that components will be added to.
     *
     * @return  a Container instance
     */
    public Container getContainer() {
        return this.container;
    }

    /**
     * Gets the name of the package to be scanned.
     *
     * @return  the package name, as a String
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Gets the set of regexes that define which types should be excluded during the component finding process.
     *
     * @return  a set of Pattern (regex) instances
     */
    public Set<Pattern> getExclusions() {
        return new HashSet<>(exclusions);
    }

    /**
     * Adds one or more regexes to the set of regexes that define which types should be excluded during the component finding process.
     *
     * @param regexes   one or more regular expressions, as Strings
     */
    public void exclude(String... regexes) {
        if (regexes != null) {
            for (String regex : regexes) {
                this.exclusions.add(Pattern.compile(regex));
            }
        }
    }

    /**
     * Clears the set of exclusions.
     */
    public void clearExclusions() {
        this.exclusions.clear();
    }

}