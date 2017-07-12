package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * This is the superclass for a number of component finder strategies.
 */
public abstract class AbstractComponentFinderStrategy implements ComponentFinderStrategy {

    private static final Log log = LogFactory.getLog(AbstractComponentFinderStrategy.class);

    protected ComponentFinder componentFinder;

    private TypeRepository typeRepository;

    protected List<SupportingTypesStrategy> supportingTypesStrategies = new ArrayList<>();

    protected AbstractComponentFinderStrategy() {
    }

    protected AbstractComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        Arrays.stream(strategies).forEach(this::addSupportingTypesStrategy);
    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
        typeRepository = new DefaultTypeRepository(componentFinder.getPackageName(), componentFinder.getExclusions());
        supportingTypesStrategies.forEach(sts -> sts.setTypeRepository(getTypeRepository()));
    }

    protected TypeRepository getTypeRepository() {
        return typeRepository;
    }

    private void findSupportingTypes(Set<Component> components) throws Exception {
        for (Component component : components) {
            for (CodeElement codeElement : component.getCode()) {
                codeElement.setVisibility(TypeUtils.getVisibility(codeElement.getType()).getName());
                codeElement.setCategory(TypeUtils.getCategory(codeElement.getType()).getName());
            }

            for (SupportingTypesStrategy strategy : supportingTypesStrategies) {
                for (String type : strategy.findSupportingTypes(component)) {
                    if (componentFinder.getContainer().getComponentOfType(type) == null) {
                        CodeElement codeElement = component.addSupportingType(type);
                        codeElement.setVisibility(TypeUtils.getVisibility(type).getName());
                        codeElement.setCategory(TypeUtils.getCategory(type).getName());
                    }
                }
            }
        }
    }

    protected abstract Set<Component> doFindComponents() throws Exception;

    private Set<Component> componentsFound = new HashSet<>();

    @Override
    public Set<Component> findComponents() throws Exception {
        componentsFound.addAll(doFindComponents());

        return componentsFound;
    }

    @Override
    public void postFindComponents() throws Exception {
        findSupportingTypes(componentsFound);

        for (Component component : componentFinder.getContainer().getComponents()) {
            if (component.getType() != null) {
                addEfferentDependencies(component, component.getType(), new HashSet<>());

                // and repeat for the supporting types
                for (CodeElement codeElement : component.getCode()) {
                    addEfferentDependencies(component, codeElement.getType(), new HashSet<>());
                }
            }
        }
    }

    private void addEfferentDependencies(Component component, String type, Set<String> typesVisited) {
        typesVisited.add(type);

        try {
            for (Class<?> referencedType : getTypeRepository().findReferencedTypes(type)) {
                Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedType.getCanonicalName());
                if (destinationComponent != null) {
                    if (component != destinationComponent) {
                        component.uses(destinationComponent, "");
                    }
                } else if (!typesVisited.contains(referencedType.getCanonicalName())) {
                    addEfferentDependencies(component, referencedType.getCanonicalName(), typesVisited);
                }
            }
        } catch (Exception e) {
            log.warn(e);
        }
    }

    protected Set<Class<?>> findTypesAnnotatedWith(Class<? extends Annotation> annotation) throws Exception {
        return TypeUtils.findTypesAnnotatedWith(annotation, getTypeRepository().getAllTypes());
    }

    protected Set<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology) throws Exception {
        return findClassesWithAnnotation(type, technology, false);
    }

    protected Set<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology, boolean includePublicTypesOnly) throws Exception {
        Set<Component> components = new HashSet<>();
        Set<Class<?>> componentTypes = findTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            if (!includePublicTypesOnly || Modifier.isPublic(componentType.getModifiers())) {
            components.add(getComponentFinder().getContainer().addComponent(
                    componentType.getSimpleName(),
                    componentType.getCanonicalName(),
                    "",
                    technology));
            }
        }

        return components;
    }

    public void addSupportingTypesStrategy(SupportingTypesStrategy supportingTypesStrategy) {
        supportingTypesStrategies.add(supportingTypesStrategy);
        supportingTypesStrategy.setTypeRepository(getTypeRepository());
    }

}