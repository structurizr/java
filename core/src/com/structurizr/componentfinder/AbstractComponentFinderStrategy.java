package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
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
    private Reflections reflections;

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
            // find dependencies of the component type itself
            addEfferentDependencies(component, component.getFullyQualifiedClassName(), 1);

            // and also find the implementations of the component (i.e. an interface was marked as a component)
            Set<String> componentImplementations = reflections.getStore().getSubTypesOf(component.getFullyQualifiedClassName());
            for (String componentImplementation : componentImplementations) {
                addEfferentDependencies(component, componentImplementation, 1);
            }
        }
    }

    private void addEfferentDependencies(Component component, String implementationType, int depth) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(implementationType);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;
                if (referencedTypeName.startsWith(componentFinder.getPackageToScan())) {

                    Component destinationComponent = componentFinder.getContainer().getComponentWithType(referencedTypeName);
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

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    protected Component getComponentWithType(String fullyQualifiedClassName) {
        return componentFinder.getContainer().getComponentWithType(fullyQualifiedClassName);
    }

}
