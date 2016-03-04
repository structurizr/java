package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * This component finder strategy knows how to find the following Spring components:
 *
 * - Classes annotated @Controller or @RestController
 * - Classes annotated @Component or @Service
 * - Classes annotated @Repository
 * - Classes that extend JpaRepository
 */
public class SpringComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public static final String SPRING_MVC_CONTROLLER = "Spring MVC Controller";
    public static final String SPRING_SERVICE = "Spring Service";
    public static final String SPRING_REPOSITORY = "Spring Repository";
    public static final String SPRING_COMPONENT = "Spring Component";
    public static final String SPRING_REST_CONTROLLER = "Spring REST Controller";

    @Override
    public Collection<Component> findComponents() throws Exception {
        Collection<Component> components = new LinkedList<>();

        components.addAll(findSpringMvcControllers());
        components.addAll(findSpringServices());
        components.addAll(findSpringRepositories());
        components.addAll(findSpringComponents());
        components.addAll(findSpringRestControllers());
        components.addAll(findSpringJpaInterfaces());

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

    protected Collection<Component> findSpringRestControllers() {
        return findClassesAnnotated(
                org.springframework.web.bind.annotation.RestController.class,
                SPRING_REST_CONTROLLER);
    }

    protected Collection<Component> findSpringJpaInterfaces() {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class> componentTypes = getInterfacesThatExtend(JpaRepository.class);

        for (Class<?> componentType : componentTypes) {
            componentsFound.add(getComponentFinder().foundComponent(
                    componentType.getCanonicalName(), null, "", SPRING_REPOSITORY, ""));

        }
        return componentsFound;
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