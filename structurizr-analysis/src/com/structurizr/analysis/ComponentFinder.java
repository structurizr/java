package com.structurizr.analysis;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class allows you to find components in a Java codebase, when used in conjunction
 * with a number of pluggable component finder strategies.
 */
public class ComponentFinder {

    private URLClassLoader urlClassLoader;
    private Container container;
    private List<String> packageNames;

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
        this.packageNames = new ArrayList<String>(Collections.singletonList(packageName));

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
            componentFinderStrategy.beforeFindComponents();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentsFound.addAll(componentFinderStrategy.findComponents());
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.afterFindComponents();
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
     * Adds a package name to be scanned
     *
     * @param packageName the package name as a String
     */
    public void addPackagesName(String packageName) {
        packageNames.add(packageName);
    }

    /**
     * Gets the names of the packages to be scanned.
     *
     * @return  the package names, as a List of String
     */
    public List<String> getPackageNames() {
        return new ArrayList<>(packageNames);
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

    /**
     * Sets a classloader to load classes from instead of the system classloader
     * @param urlClassLoader the classloader to use
     */
    public void setUrlClassLoader(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    /**
     * Gets the classloader used to load classes
     * @return the classloader to use, or null if system classloader
     */
    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }
}