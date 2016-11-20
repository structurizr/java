package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * <p>This component finder strategy knows how to find the following Spring components:</p>
 * <ul>
 * <li>Classes annotated {@link org.springframework.stereotype.Controller} or {@link org.springframework.web.bind.annotation.RestController}</li>
 * <li> Classes annotated {@link org.springframework.stereotype.Component} or {@link org.springframework.stereotype.Service}</li>
 * <li> Classes annotated {@link org.springframework.data.repository.Repository}</li>
 * <li> Classes that extend the {@link JpaRepository} or {@link CrudRepository} interface</li>
 * </ul>
 * <p>Non-public types will be ignored so that, for example, you can
 * hide repository implementations behind services.</p>
 * <ul>
 * <li>e.g. http://olivergierke.de/2013/01/whoops-where-did-my-architecture-go/</li>
 * </ul>
 *
 * @author Simon Brown
 */
public class SpringComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public static final String SPRING_MVC_CONTROLLER = "Spring MVC Controller";
    public static final String SPRING_SERVICE = "Spring Service";
    public static final String SPRING_REPOSITORY = "Spring Repository";
    public static final String SPRING_COMPONENT = "Spring Component";
    public static final String SPRING_REST_CONTROLLER = "Spring REST Controller";

    public SpringComponentFinderStrategy() {
    }

    public SpringComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(findAnnotatedSpringMvcControllers());
        addAll(findAnnotatedSpringRestControllers());
        addAll(findAnnotatedSpringServices());
        addAll(findAnnotatedSpringRepositories());
        addAll(findAnnotatedSpringComponents());
        addAll(findSpringRepositoryInterfaces());
    }

    protected Collection<Component> findAnnotatedSpringMvcControllers() {
        return findClassesWithAnnotation(
                org.springframework.stereotype.Controller.class, SPRING_MVC_CONTROLLER);
    }

    protected Collection<Component> findAnnotatedSpringServices() {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Service.class, SPRING_SERVICE);
    }

    protected Collection<Component> findAnnotatedSpringRepositories() {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Repository.class, SPRING_REPOSITORY);
    }

    protected Collection<Component> findAnnotatedSpringComponents() {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Component.class, SPRING_COMPONENT);
    }

    protected Collection<Component> findAnnotatedSpringRestControllers() {
        return findClassesWithAnnotation(
                org.springframework.web.bind.annotation.RestController.class,
                SPRING_REST_CONTROLLER);
    }

    protected Collection<Component> findSpringRepositoryInterfaces() {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class> componentTypes = getInterfacesThatExtend(Repository.class);
        componentTypes.addAll(getInterfacesThatExtend(JpaRepository.class));
        componentTypes.addAll(getInterfacesThatExtend(CrudRepository.class));

        for (Class<?> componentType : componentTypes) {
            if (Modifier.isPublic(componentType.getModifiers())) {
                componentsFound.add(getComponentFinder().getContainer().addComponent(
                        componentType.getSimpleName(),
                        componentType.getCanonicalName(),
                        "",
                        SPRING_REPOSITORY));
            }
        }
        return componentsFound;
    }

    protected Collection<Component> findInterfacesForImplementationClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        Collection<Component> components = new LinkedList<>();

        Set<Class<?>> annotatedTypes = getTypesAnnotatedWith(type);
        for (Class<?> annotatedType : annotatedTypes) {
            if (annotatedType.isInterface()) {
                // the annotated type is an interface, so we're done
                components.add(getComponentFinder().getContainer().addComponent(
                        annotatedType.getSimpleName(), annotatedType.getCanonicalName(), "", technology));
            } else {
                // The Spring @Component, @Service and @Repository annotations are typically used to annotate implementation
                // classes, but we really want to find the interface type and use that to represent the component. Why?
                // Well, for example, a Spring MVC controller may have a dependency on a "SomeRepository" interface, but
                // it's the "JdbcSomeRepositoryImpl" implementation class that gets annotated with @Repository.
                //
                // This next bit of code tries to find the "SomeRepository" interface...
                String componentName = annotatedType.getSimpleName(); // e.g. JdbcSomeRepositoryImpl
                String componentType = annotatedType.getCanonicalName();
                boolean foundInterface = false;

                if (annotatedType.getInterfaces().length > 0) {
                    for (Class interfaceType : annotatedType.getInterfaces()) {
                        String interfaceName = interfaceType.getSimpleName();
                        if (componentName.startsWith(interfaceName) || // <InterfaceName><***>
                                componentName.endsWith(interfaceName) ||   // <***><InterfaceName>
                                componentName.contains(interfaceName)) {   // <***><InterfaceName><***>
                            componentName = interfaceName;
                            componentType = interfaceType.getCanonicalName();
                            foundInterface = true;
                            break;
                        }
                    }
                }

                Component component = getComponentFinder().getContainer().addComponent(componentName, componentType, "", technology);
                components.add(component);

                if (foundInterface) {
                    // the primary component type is now an interface, so add the type we originally found as a supporting type
                    component.addSupportingType(annotatedType.getCanonicalName());
                }
            }
        }

        return components;
    }

}