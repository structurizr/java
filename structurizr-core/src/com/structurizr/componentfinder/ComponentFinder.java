package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentFinder {

    private Container container;
    private String packageToScan;
    private Set<Pattern> exclusions = new HashSet<>(Arrays.asList(
            Pattern.compile("java\\..*"),
            Pattern.compile("javax\\..*"),
            Pattern.compile("sun\\..*")
    ));

    private List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    public ComponentFinder(Container container, String packageToScan, ComponentFinderStrategy... componentFinderStrategies) {
        this.container = container;
        this.packageToScan = packageToScan;

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            this.componentFinderStrategies.add(componentFinderStrategy);
            componentFinderStrategy.setComponentFinder(this);
        }
    }

    public Collection<Component> findComponents() throws Exception {
        Collection<Component> componentsFound = new LinkedList<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findComponents();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findDependencies();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentsFound.addAll(componentFinderStrategy.getComponents());
        }

        return componentsFound;
    }

    public Container getContainer() {
        return this.container;
    }

    public String getPackageToScan() {
        return packageToScan;
    }

    public void exclude(String... regexes) {
        if (regexes != null) {
            for (String regex : regexes) {
                this.exclusions.add(Pattern.compile(regex));
            }
        }
    }

    public Set<Pattern> getExclusions() {
        return new HashSet<>(exclusions);
    }

}
