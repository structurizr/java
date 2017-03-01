package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * A component finder strategy for Spring Repositories (classes annotated @Repository,
 * plus those that extend JpaRepository or CrudRepository).
 *
 * @author Simon Brown
 */
public final class SpringRepositoryComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    public SpringRepositoryComponentFinderStrategy() {
    }

    public SpringRepositoryComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void findComponents() throws Exception {
        addAll(findAnnotatedSpringRepositories());
        addAll(findSpringRepositoryInterfaces());
    }

    private Collection<Component> findSpringRepositoryInterfaces() {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class> componentTypes = getInterfacesThatExtend(Repository.class);
        componentTypes.addAll(getInterfacesThatExtend(JpaRepository.class));
        componentTypes.addAll(getInterfacesThatExtend(CrudRepository.class));

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

    private Collection<Component> findAnnotatedSpringRepositories() {
        return findInterfacesForImplementationClassesWithAnnotation(
                org.springframework.stereotype.Repository.class, SPRING_REPOSITORY);
    }

}