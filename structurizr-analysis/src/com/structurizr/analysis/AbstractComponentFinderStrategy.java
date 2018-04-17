package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
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

    private TypeRepository typeRepository;

    private Set<Component> componentsFound = new HashSet<>();

    protected ComponentFinder componentFinder;

    protected List<SupportingTypesStrategy> supportingTypesStrategies = new ArrayList<>();

    protected AbstractComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        Arrays.stream(strategies).forEach(this::addSupportingTypesStrategy);
    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    /**
     * Sets a reference to the parent component finder.
     *
     * @param componentFinder   a ComponentFinder instance
     */
    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
    }

    protected TypeRepository getTypeRepository() {
        return componentFinder.getTypeRepository();
    }

    @Override
    public void beforeFindComponents() {
        supportingTypesStrategies.forEach(sts -> sts.setTypeRepository(getTypeRepository()));
    }

    @Override
    public Set<Component> findComponents() {
        componentsFound.addAll(doFindComponents());

        return componentsFound;
    }

    /**
     * A template method into which subclasses can put their component finding code.
     *
     * @return  the Set of Components found, or an empty set if no components were found
     */
    protected abstract Set<Component> doFindComponents();

    @Override
    public void afterFindComponents() {
        findSupportingTypes(componentsFound);
        findDependencies();
    }

    private void findSupportingTypes(Set<Component> components) {
        for (Component component : components) {
            for (CodeElement codeElement : component.getCode()) {
                TypeVisibility visibility = TypeUtils.getVisibility(getTypeRepository(), codeElement.getType());
                if (visibility != null) {
                    codeElement.setVisibility(visibility.getName());
                }

                TypeCategory category = TypeUtils.getCategory(getTypeRepository(), codeElement.getType());
                if (category != null) {
                    codeElement.setCategory(category.getName());
                }
            }

            for (SupportingTypesStrategy strategy : supportingTypesStrategies) {
                for (Class<?> type : strategy.findSupportingTypes(component)) {
                    if (!isNestedClass(type) && componentFinder.getContainer().getComponentOfType(type.getCanonicalName()) == null) {
                        CodeElement codeElement = component.addSupportingType(type.getCanonicalName());

                        TypeVisibility visibility = TypeUtils.getVisibility(getTypeRepository(), codeElement.getType());
                        if (visibility != null) {
                            codeElement.setVisibility(visibility.getName());
                        }

                        TypeCategory category = TypeUtils.getCategory(getTypeRepository(), codeElement.getType());
                        if (category != null) {
                            codeElement.setCategory(category.getName());
                        }
                    }
                }
            }
        }
    }

    private boolean isNestedClass(Class<?> type) {
        return type != null && type.getName().indexOf('$') > -1;
    }

    private void findDependencies() {
        for (Component component : componentFinder.getContainer().getComponents()) {
            for (CodeElement codeElement : component.getCode()) {
                addEfferentDependencies(component, codeElement.getType(), new HashSet<>());
            }
        }
    }

    private void addEfferentDependencies(Component component, String type, Set<String> typesVisited) {
        typesVisited.add(type);

        for (Class<?> referencedType : getTypeRepository().findReferencedTypes(type)) {
            try {
                String referencedTypeName = referencedType.getCanonicalName();
                Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedTypeName);
                if (destinationComponent != null) {
                    if (component != destinationComponent) {
                        component.uses(destinationComponent, "");
                    }
                } else if (!typesVisited.contains(referencedTypeName)) {
                    addEfferentDependencies(component, referencedTypeName, typesVisited);
                }
            } catch (Throwable t) {
                log.warn(t);
            }
        }
    }

    /**
     * Adds a supporting type strategy to this component finder strategy.
     *
     * @param supportingTypesStrategy   a SupportingTypesStrategy instance
     */
    public void addSupportingTypesStrategy(SupportingTypesStrategy supportingTypesStrategy) {
        if (supportingTypesStrategy == null) {
            throw new IllegalArgumentException("A supporting types strategy must be provided.");
        }

        supportingTypesStrategies.add(supportingTypesStrategy);
    }

    protected Set<Class<?>> findTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return TypeUtils.findTypesAnnotatedWith(annotation, getTypeRepository().getAllTypes());
    }

    protected Set<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        return findClassesWithAnnotation(type, technology, false);
    }

    protected Set<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology, boolean includePublicTypesOnly) {
        Set<Component> components = new HashSet<>();
        Set<Class<?>> componentTypes = findTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            if (!includePublicTypesOnly || Modifier.isPublic(componentType.getModifiers())) {
                final Container container = getComponentFinder().getContainer();
                if (container.getComponentWithName(componentType.getSimpleName()) == null) {
                    components.add(container.addComponent(
                        componentType.getSimpleName(),
                        componentType.getCanonicalName(),
                        "",
                        technology));
                }
            }
        }

        return components;
    }

}
