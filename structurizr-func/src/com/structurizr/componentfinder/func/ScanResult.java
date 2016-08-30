package com.structurizr.componentfinder.func;

import com.google.common.collect.ImmutableSet;
import com.structurizr.model.Component;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ScanResult implements Iterable<Component> {
    private final Reflections reflections;
    private final Collection<Component> components;


    public ScanResult(Reflections reflections, Collection<Component> components) {
        this.reflections = reflections;
        this.components = ImmutableSet.copyOf(components);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    public Stream<Component> stream() {
        return StreamSupport.stream(spliterator(), false);
    }


    public Optional<Class> getFirstImplementationOfInterface(String interfaceType) {
        return getFirstImplementationOfInterface(getClass(interfaceType));
    }

    public Optional<Class> getFirstImplementationOfInterface(Class interfaceType) {
        final Set<Class> implementationClasses = reflections.getSubTypesOf(interfaceType);
        if (implementationClasses.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(implementationClasses.iterator().next());
        }
    }

    public Optional<Component> getFirstComponentWithName(String name) {
        if (name == null)
            return Optional.empty();
        else
            return components.stream().filter(c -> name.equals(c.getName())).findFirst();
    }

    public Optional<Component> getFirstComponentOfType(String type) {
        if (type == null)
            return Optional.empty();
        else
            return components.stream().filter(c -> type.equals(c.getType())).findFirst();
    }

    public Collection<Component> getComponents() {
        return Collections.unmodifiableCollection(components);
    }


    private Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
