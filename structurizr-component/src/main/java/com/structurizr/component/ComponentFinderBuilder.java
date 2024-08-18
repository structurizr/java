package com.structurizr.component;

import com.structurizr.component.provider.DirectoryTypeProvider;
import com.structurizr.component.provider.JarFileTypeProvider;
import com.structurizr.component.provider.SourceCodeTypeProvider;
import com.structurizr.component.provider.TypeProvider;
import com.structurizr.model.Container;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to create a {@link ComponentFinder} instance.
 */
public class ComponentFinderBuilder {

    private Container container;
    private final List<TypeProvider> typeProviders = new ArrayList<>();
    private final List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    public ComponentFinderBuilder forContainer(Container container) {
        this.container = container;

        return this;
    }

    public ComponentFinderBuilder fromJarFile(String filename) {
        return fromJarFile(new File(filename));
    }

    public ComponentFinderBuilder fromJarFile(File file) {
        this.typeProviders.add(new JarFileTypeProvider(file));

        return this;
    }

    public ComponentFinderBuilder fromDirectory(String path) {
        return fromDirectory(new File(path));
    }

    public ComponentFinderBuilder fromDirectory(File path) {
        this.typeProviders.add(new DirectoryTypeProvider(path));

        return this;
    }

    public ComponentFinderBuilder fromSourceCode(String path) {
        return fromSourceCode(new File(path));
    }

    public ComponentFinderBuilder fromSourceCode(File path) {
        this.typeProviders.add(new SourceCodeTypeProvider(path));

        return this;
    }

    public ComponentFinderBuilder withStrategy(ComponentFinderStrategy componentFinderStrategy) {
        this.componentFinderStrategies.add(componentFinderStrategy);

        return this;
    }

    public ComponentFinder build() {
        return new ComponentFinder(container, typeProviders, componentFinderStrategies);
    }

}
