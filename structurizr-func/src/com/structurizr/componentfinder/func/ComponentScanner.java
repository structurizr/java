package com.structurizr.componentfinder.func;

import com.google.common.collect.ImmutableSet;
import com.structurizr.model.Component;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.*;

class ComponentScanner {

    private final Collection<TypeBasedComponentFactory> factories;


    ComponentScanner(ImmutableSet<TypeBasedComponentFactory> factories) {
        this.factories = factories;
    }


    ScanResult scanForComponents(String packageToScan) {
        validatePackageString(packageToScan);
        final Reflections reflections = createReflections(packageToScan);
        final Collection<Component> c = createComponents(reflections);
        return new ScanResult(reflections, c);
    }

    private Collection<Component> createComponents(Reflections reflections) {
        final Collection<Component> c = new HashSet<>();
        final Set<Class<?>> types = getAllTypes(reflections);
        types.forEach(x -> c.addAll(createComponents(x)));
        return c;
    }

    private Collection<Component> createComponents(Class<?> type) {
        final Collection<Component> c = new LinkedList<>();
        factories.forEach(x -> x.createComponent(type).ifPresent(c::add));
        return Collections.unmodifiableCollection(c);
    }


    private Reflections createReflections(String packageToScan) {
        return new Reflections(new ConfigurationBuilder()
                .filterInputsBy(new FilterBuilder().includePackage(packageToScan))
                .setUrls(ClasspathHelper.forPackage(packageToScan))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false), new FieldAnnotationsScanner()));
    }


    private Set<Class<?>> getAllTypes(Reflections reflections) {
        return reflections.getSubTypesOf(Object.class);
    }

    private void validatePackageString(String packageToScan) {
        PackageNameValidator.INSTANCE.accept(packageToScan);
    }


}
