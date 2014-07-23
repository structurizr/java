package com.structurizr.componentfinder;

import java.lang.annotation.Annotation;
import java.util.Set;

public class SpringComponentFinderStrategy extends AbstractComponentFinderStrategy {

    @Override
    public void findComponents() throws Exception {
        findClassesAnnotated(org.springframework.stereotype.Controller.class, "Spring Controller");
        findImplementationClassesAnnotated(org.springframework.stereotype.Service.class, "Spring Service");
        findImplementationClassesAnnotated(org.springframework.stereotype.Repository.class, "Spring Repository");
        findImplementationClassesAnnotated(org.springframework.stereotype.Component.class, "Spring Component");
    }

    private void findClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            getComponentFinder().foundComponent(null, componentType.getCanonicalName(), "", technology);
        }
    }

    private void findImplementationClassesAnnotated(Class<? extends Annotation> type, String technology) {
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            // WARNING: this code makes an assumption that the first implemented interface is the component type
            // i.e. JdbcSomethingRepository (annotated @Repository) *only* implements SomethingRepository,
            // which is the component type we're interested in
            getComponentFinder().foundComponent(componentType.getInterfaces()[0].getCanonicalName(), componentType.getCanonicalName(), "", technology);
        }
    }

}