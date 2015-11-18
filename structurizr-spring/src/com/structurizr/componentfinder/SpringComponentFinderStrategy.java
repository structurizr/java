package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class SpringComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public static final String SPRING_MVC_CONTROLLER = "Spring MVC Controller";
    public static final String SPRING_SERVICE = "Spring Service";
    public static final String SPRING_REPOSITORY = "Spring Repository";
    public static final String SPRING_COMPONENT = "Spring Component";

    @Override
    public Collection<Component> findComponents() throws Exception {
        Collection<Component> components = new LinkedList<>();

        components.addAll(findSpringMvcControllers());
        components.addAll(findSpringServices());
        components.addAll(findSpringRepositories());
        components.addAll(findSpringComponents());

        return components;
    }

    protected Collection<Component> findSpringMvcControllers() {
        return findClassesAnnotated(
                org.springframework.stereotype.Controller.class, SPRING_MVC_CONTROLLER);
    }

    protected Collection<Component> findSpringServices() {
        return findImplementationClassesAnnotated(
                org.springframework.stereotype.Service.class, SPRING_SERVICE);
    }

    protected Collection<Component> findSpringRepositories() {
        return findImplementationClassesAnnotated(
                org.springframework.stereotype.Repository.class, SPRING_REPOSITORY);
    }

    protected Collection<Component> findSpringComponents() {
        return findImplementationClassesAnnotated(
                org.springframework.stereotype.Component.class, SPRING_COMPONENT);
    }

    protected Collection<Component> findClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Collection<Component> components = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            components.add(getComponentFinder().foundComponent(
                    null, componentType.getCanonicalName(), "", technology, ""));

        }
        return components;
    }

    protected Collection<Component> findImplementationClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Collection<Component> components = new LinkedList<>();

        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            // WARNING: this code makes an assumption that the first implemented interface is the component type
            // i.e. JdbcSomethingRepository (annotated @Repository) *only* implements SomethingRepository,
            // which is the component type we're interested in
            String interfaceName = null;
            if (componentType.getInterfaces().length > 0) {
                interfaceName = componentType.getInterfaces()[0].getCanonicalName();
            }
            components.add(getComponentFinder().foundComponent(
                    interfaceName, componentType.getCanonicalName(), "", technology, ""));
        }

        return components;
    }

}