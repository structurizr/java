package com.structurizr.componentfinder;

import com.google.common.base.Predicates;
import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This is the superclass for a number of component finder strategies, which itself is
 * based upon the Reflections library (https://github.com/ronmamo/reflections).
 */
public abstract class AbstractReflectionsComponentFinderStrategy extends ComponentFinderStrategy {

    private static HashMap<ComponentFinder, Reflections> REFLECTIONS = new HashMap<>();

    protected List<SupportingTypesStrategy> supportingTypesStrategies = new ArrayList<>();

    private Map<String,Set<String>> referencedTypesCache = new HashMap<>();

    public AbstractReflectionsComponentFinderStrategy() {
    }

    protected AbstractReflectionsComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        Arrays.stream(strategies).forEach(this::addSupportingTypesStrategy);
    }

    @Override
    public void setComponentFinder(ComponentFinder componentFinder) {
        super.setComponentFinder(componentFinder);

        if (!REFLECTIONS.containsKey(componentFinder)) {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .filterInputsBy(new FilterBuilder().includePackage(componentFinder.getPackageToScan()))
                    .setUrls(ClasspathHelper.forJavaClassPath())
                    .setScanners(
                            new TypeAnnotationsScanner(),
                            new SubTypesScanner(false),
                            new FieldAnnotationsScanner(),
                            new AllTypesScanner()
                    )
            );

            REFLECTIONS.put(componentFinder, reflections);
        }
    }

    protected Reflections getReflections() {
        return REFLECTIONS.get(componentFinder);
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
                for (CodeElement codeElement : component.getCode()) {
                    addEfferentDependencies(component, codeElement.getType(), new HashSet<>());
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

        // use the cached version if possible
        if (referencedTypesCache.containsKey(type)) {
            return referencedTypesCache.get(type);
        }

        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass cc = pool.get(type);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;

                if (!isExcluded(referencedTypeName)) {
                    referencedTypeNames.add(referencedTypeName);
                }
            }
        } catch (NotFoundException e) {
            System.err.println("Could not find " + type + " ... ignoring.");
        }

        // cache for the next time
        referencedTypesCache.put(type, referencedTypeNames);

        return referencedTypeNames;
    }

    private boolean isExcluded(String typeName) {
        for (Pattern exclude : componentFinder.getExclusions()) {
            if (exclude.matcher(typeName).matches()) {
                return true;
            }
        }

        return false;
    }

    private Set<Class<?>> filter(Set<Class<?>> types) {
        return types.stream().filter(c -> !isExcluded(c.getCanonicalName())).collect(Collectors.toSet());
    }

    protected Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return filter(getReflections().getTypesAnnotatedWith(annotation));
    }

    protected Set<Class<?>> getAllTypes() {
        return filter(getReflections().getSubTypesOf(Object.class));
    }

    protected Set<String> getAllTypeNames() {
        return getReflections().getStore().get(AllTypesScanner.class.getSimpleName()).keySet();
    }

    protected Set<Class> getInterfacesThatExtend(Class interfaceType) {
        return filter(getReflections().getSubTypesOf(interfaceType));
    }

    protected Class getFirstImplementationOfInterface(String interfaceTypeName) throws Exception {
        return getFirstImplementationOfInterface(Class.forName(interfaceTypeName));
    }

    protected Class getFirstImplementationOfInterface(Class interfaceType) throws Exception {
        Set<Class> implementationClasses = filter(getReflections().getSubTypesOf(interfaceType));

        if (implementationClasses.isEmpty()) {
            return null;
        } else {
            return implementationClasses.iterator().next();
        }
    }

    protected Set<Class<?>> findSuperTypesAnnotatedWith(Class<?> implementationType, Class annotation) {
        return filter(ReflectionUtils.getAllSuperTypes(implementationType, Predicates.and(ReflectionUtils.withAnnotation(annotation))));
    }

    protected Collection<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        return findClassesWithAnnotation(type, technology, false);
    }

    protected Collection<Component> findClassesWithAnnotation(Class<? extends Annotation> type, String technology, boolean includePublicTypesOnly) {
        Collection<Component> components = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
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
        supportingTypesStrategy.setComponentFinderStrategy(this);
    }

    class AllTypesScanner extends AbstractScanner {

        @Override
        public boolean acceptResult(String fqn) {
            return super.acceptResult(fqn) && !isExcluded(fqn);
        }

        @Override
        public void scan(Object cls) {
            String className = getMetadataAdapter().getClassName(cls);

            getStore().put(className, className);
        }

    }

}