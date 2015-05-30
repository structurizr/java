package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class SpringComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    @Override
    public Collection<Component> findComponents() throws Exception {
        Collection<Component> componentsFound = new LinkedList<>();

        componentsFound.addAll(findClassesAnnotated(org.springframework.stereotype.Controller.class, "Spring MVC Controller"));
        componentsFound.addAll(findImplementationClassesAnnotated(org.springframework.stereotype.Service.class, "Spring Service"));
        componentsFound.addAll(findImplementationClassesAnnotated(org.springframework.stereotype.Repository.class, "Spring Repository"));
        componentsFound.addAll(findImplementationClassesAnnotated(org.springframework.stereotype.Component.class, "Spring Component"));

        return componentsFound;
    }

    private Collection<Component> findClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            componentsFound.add(getComponentFinder().foundComponent(null, componentType.getCanonicalName(), "", technology, ""));

        }
        return componentsFound;
    }

    private Collection<Component> findImplementationClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            // WARNING: this code makes an assumption that the first implemented interface is the component type
            // i.e. JdbcSomethingRepository (annotated @Repository) *only* implements SomethingRepository,
            // which is the component type we're interested in
            componentsFound.add(getComponentFinder().foundComponent(componentType.getInterfaces()[0].getCanonicalName(), componentType.getCanonicalName(), "", technology, ""));
        }
        return componentsFound;
    }

}