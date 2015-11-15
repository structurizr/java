package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * This extends the Spring component finder to add support for the Spring components
 * that make up a JHipster application:
 *  - Spring REST controllers
 *  - JPA repositories
 *
 *  Please note: this implementation only finds components in the Java (Spring Boot)
 *  part of a JHipster application. A future version will (hopefully!) extract components
 *  from the AngularJS part too.
 */
public class JHipsterComponentFinderStrategy extends SpringComponentFinderStrategy {

    public static final String SPRING_REST_CONTROLLER = "Spring REST Controller";

    @Override
    public Collection<Component> findComponents() throws Exception {
        Collection<Component> components = super.findComponents();

        components.addAll(findSpringRestControllers());
        components.addAll(findSpringJpaInterfaces());

        return components;
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

}