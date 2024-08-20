package com.structurizr.component;

import com.structurizr.component.provider.ClassDirectoryTypeProvider;
import com.structurizr.component.provider.ClassJarFileTypeProvider;
import com.structurizr.component.provider.SourceDirectoryTypeProvider;
import com.structurizr.component.provider.TypeProvider;
import com.structurizr.model.Container;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to create a {@link ComponentFinder} instance.
 */
public class ComponentFinderBuilder {

    private static final String JAR_FILE_EXTENSION = ".jar";

    private Container container;
    private final List<TypeProvider> typeProviders = new ArrayList<>();
    private final List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    public ComponentFinderBuilder forContainer(Container container) {
        this.container = container;

        return this;
    }

    public ComponentFinderBuilder fromClasses(String path) {
        return fromClasses(new File(path));
    }

    public ComponentFinderBuilder fromClasses(File path) {
        if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist");
        }

        if (path.isDirectory()) {
            this.typeProviders.add(new ClassDirectoryTypeProvider(path));
        } else if (path.getName().endsWith(JAR_FILE_EXTENSION)) {
            this.typeProviders.add(new ClassJarFileTypeProvider(path));
        } else {
            throw new IllegalArgumentException("Expected a directory of classes or a .jar file: " + path.getAbsolutePath());
        }

        return this;
    }

    public ComponentFinderBuilder fromSource(String path) {
        return fromSource(new File(path));
    }

    public ComponentFinderBuilder fromSource(File path) {
        this.typeProviders.add(new SourceDirectoryTypeProvider(path));

        return this;
    }

    public ComponentFinderBuilder withStrategy(ComponentFinderStrategy componentFinderStrategy) {
        this.componentFinderStrategies.add(componentFinderStrategy);

        return this;
    }

    public ComponentFinder build() {
        if (container == null) {
            throw new RuntimeException("A container must be specified");
        }

        if (typeProviders.isEmpty()) {
            throw new RuntimeException("One or more type providers must be configured");
        }

        if (componentFinderStrategies.isEmpty()) {
            throw new RuntimeException("One or more component finder strategies must be configured");
        }

        return new ComponentFinder(container, typeProviders, componentFinderStrategies);
    }

}
