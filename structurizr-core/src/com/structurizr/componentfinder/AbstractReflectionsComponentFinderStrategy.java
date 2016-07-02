package com.structurizr.componentfinder;

import com.google.common.base.Predicates;
import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the superclass for a number of component finder strategies, which itself is
 * based upon the Reflections library (https://github.com/ronmamo/reflections).
 */
public abstract class AbstractReflectionsComponentFinderStrategy extends ComponentFinderStrategy {

    protected Reflections reflections;

    protected List<SupportingTypesStrategy> supportingTypesStrategies = new ArrayList<>();

    public AbstractReflectionsComponentFinderStrategy() {
    }

    protected AbstractReflectionsComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        this.supportingTypesStrategies = Arrays.asList(strategies);
        this.supportingTypesStrategies.stream().forEach(s -> s.setComponentFinderStrategy(this));
    }

    @Override
    public void setComponentFinder(ComponentFinder componentFinder) {
        super.setComponentFinder(componentFinder);

        this.reflections = new Reflections(new ConfigurationBuilder()
                  .filterInputsBy(new FilterBuilder().includePackage(componentFinder.getPackageToScan()))
                  .setUrls(ClasspathHelper.forPackage(componentFinder.getPackageToScan()))
                  .setScanners(
                          new TypeAnnotationsScanner(),
                          new SubTypesScanner(false),
                          new FieldAnnotationsScanner())
                  );
    }

    @Override
    public void findDependencies() throws Exception {
        // before finding dependencies, let's find the types that are used to implement each component
        for (Component component : getComponents()) {
            for (SupportingTypesStrategy strategy : supportingTypesStrategies) {
                for (String type : strategy.getSupportingTypes(component)) {
                    if (componentFinder.getContainer().getComponentOfType(type) == null) {
                        component.addSupportingType(type);
                    }
                }
            }
        }

        for (Component component : componentFinder.getContainer().getComponents()) {
            if (component.getType() != null) {
                addEfferentDependencies(component, component.getType(), new HashSet<>());

                // and repeat for the supporting types
                for (String supportingType : component.getSupportingTypes()) {
                    addEfferentDependencies(component, supportingType, new HashSet<>());
                }
            }
        }
    }

    private void addEfferentDependencies(Component component, String type, Set<String> typesVisited) {
        typesVisited.add(type);

        try {
            for (String referencedTypeName : getReferencedTypes(type)) {
                Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedTypeName);
                if (destinationComponent != null) {
                    if (component != destinationComponent) {
                        component.uses(destinationComponent, "");
                    }
                } else if (!typesVisited.contains(referencedTypeName)) {
                    addEfferentDependencies(component, referencedTypeName, typesVisited);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    protected Set<String> getReferencedTypesInPackage(String type) throws Exception {
        return getReferencedTypes(type).stream()
                .filter(s -> s.startsWith(componentFinder.getPackageToScan()))
                .collect(Collectors.toSet());
    }

    protected Set<String> getReferencedTypes(String type) throws Exception {
        Set<String> referencedTypeNames = new HashSet<>();

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(type);
        for (Object referencedType : cc.getRefClasses()) {
            String referencedTypeName = (String)referencedType;

            if (!isAJavaPlatformType(referencedTypeName)) {
                referencedTypeNames.add(referencedTypeName);
            }
        }

        return referencedTypeNames;
    }

    private boolean isAJavaPlatformType(String typeName) {
        return  typeName.startsWith("java.") ||
                typeName.startsWith("javax.") ||
                typeName.startsWith("sun.");
    }

    protected Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    protected Set<Class<?>> getAllTypes() {
        return reflections.getSubTypesOf(Object.class);
    }

    protected Set<Class> getInterfacesThatExtend(Class interfaceType) {
        return reflections.getSubTypesOf(interfaceType);
    }

    protected Class getFirstImplementationOfInterface(String interfaceTypeName) throws Exception {
        return getFirstImplementationOfInterface(Class.forName(interfaceTypeName));
    }

    protected Class getFirstImplementationOfInterface(Class interfaceType) throws Exception {
        Set<Class> implementationClasses = reflections.getSubTypesOf(interfaceType);

        if (implementationClasses.isEmpty()) {
            return null;
        } else {
            return implementationClasses.iterator().next();
        }
    }

    protected Set<Class<?>> findSuperTypesAnnotatedWith(Class<?> implementationType, Class annotation) {
        return ReflectionUtils.getAllSuperTypes(implementationType, Predicates.and(ReflectionUtils.withAnnotation(annotation)));
    }

    protected Collection<Component> findPublicClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        Collection<Component> components = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            if (Modifier.isPublic(componentType.getModifiers())) {
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
    }

}
