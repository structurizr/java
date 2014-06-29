package com.structurizr.componentfinder;

import com.google.common.base.Predicates;
import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public abstract class AbstractComponentFinderStrategy implements ComponentFinderStrategy {

    private ComponentFinder componentFinder;
    protected Reflections reflections;

    public AbstractComponentFinderStrategy() {
    }

    @Override
    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;

        this.reflections = new Reflections(new ConfigurationBuilder()
                  .filterInputsBy(new FilterBuilder().includePackage(componentFinder.getPackageToScan()))
                  .setUrls(ClasspathHelper.forPackage(componentFinder.getPackageToScan()))
                  .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner()));
    }

    @Override
    public void findDependencies() throws Exception {
        for (Component component : componentFinder.getContainer().getComponents()) {
            addEfferentDependencies(component, component.getInterfaceType(), 1);
            addEfferentDependencies(component, component.getImplementationType(), 1);
        }
    }

    private void addEfferentDependencies(Component component, String implementationType, int depth) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(implementationType);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;
                if (referencedTypeName.startsWith(componentFinder.getPackageToScan())) {

                    Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedTypeName);
                    if (destinationComponent != null) {
                        if (component != destinationComponent) {
                            component.uses(destinationComponent, "");
                        }
                    } else if (!referencedTypeName.equals(implementationType) && depth < 10) {
                        addEfferentDependencies(component, referencedTypeName, ++depth);
                    }
                }
            }
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
    }

    protected Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    protected Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getFieldsAnnotatedWith(annotation);
    }

    protected Set<Class<?>> findSuperTypesAnnotatedWith(Class<?> implementationType, Class annotation) {
        return ReflectionUtils.getAllSuperTypes(implementationType, Predicates.and(ReflectionUtils.withAnnotation(annotation)));

    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    protected Component getComponentWithType(String fullyQualifiedClassName) {
        return componentFinder.getContainer().getComponentOfType(fullyQualifiedClassName);
    }

}
