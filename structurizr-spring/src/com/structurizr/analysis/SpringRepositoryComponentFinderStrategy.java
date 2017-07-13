package com.structurizr.analysis;

import com.structurizr.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A component finder strategy for Spring repositories (classes annotated @Repository,
 * plus those that extend JpaRepository or CrudRepository).
 */
public final class SpringRepositoryComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringRepositoryComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        Set<Component> components = new HashSet<>();

        components.addAll(findAnnotatedSpringRepositories());
        components.addAll(findSpringRepositoryInterfaces());

        return components;
    }

    private Set<Component> findSpringRepositoryInterfaces() throws Exception {
        Set<Component> componentsFound = new HashSet<>();
        Set<Class<?>> componentTypes = new HashSet<>();

        Set<Class<?>> types = getTypeRepository().getAllTypes();
        for (Class<?> type : types) {
            if (type.isInterface()) {
                if  (
                        Repository.class.isAssignableFrom(type) ||
                        JpaRepository.class.isAssignableFrom(type) ||
                        CrudRepository.class.isAssignableFrom(type)
                ) {
                    componentTypes.add(type);
                }
            }
        }

        for (Class<?> componentType : componentTypes) {
            if (!includePublicTypesOnly || Modifier.isPublic(componentType.getModifiers())) {
                componentsFound.add(getComponentFinder().getContainer().addComponent(
                        componentType.getSimpleName(),
                        componentType.getCanonicalName(),
                        "",
                        SPRING_REPOSITORY));
            }
        }

        return componentsFound;
    }

    private Set<Component> findAnnotatedSpringRepositories() throws Exception {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Repository.class, SPRING_REPOSITORY);
    }

}